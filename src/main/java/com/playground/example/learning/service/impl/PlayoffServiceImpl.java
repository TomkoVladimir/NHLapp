package com.playground.example.learning.service.impl;

import com.playground.example.learning.dto.PlayoffDto.PlayoffRequestDto;
import com.playground.example.learning.dto.PlayoffDto.PlayoffResponseDto;
import com.playground.example.learning.dto.SeriesDto.SeriesResponseDto;
import com.playground.example.learning.entity.Player;
import com.playground.example.learning.entity.Playoff;
import com.playground.example.learning.entity.Series;
import com.playground.example.learning.entity.Team;
import com.playground.example.learning.events.SeriesCreatedEvent;
import com.playground.example.learning.exception.ResourceNotFoundException;
import com.playground.example.learning.mapper.SeriesMapper;
import com.playground.example.learning.repository.PlayerRepository;
import com.playground.example.learning.repository.PlayoffRepository;
import com.playground.example.learning.repository.SeriesRepository;
import com.playground.example.learning.repository.TeamRepository;
import com.playground.example.learning.service.PlayoffService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class PlayoffServiceImpl implements PlayoffService
{
    private final PlayoffRepository playoffRepository;
    private final SeriesRepository seriesRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    @Override
    @Transactional
    public PlayoffResponseDto createPlayoff(PlayoffRequestDto requestDto)
    {
        // Fetch players
        Player playerOne = playerRepository.findByNickName(requestDto.getPlayerOneNickName())
            .orElseThrow(() -> new ResourceNotFoundException("Player one not found"));

        Player playerTwo = playerRepository.findByNickName(requestDto.getPlayerTwoNickName())
            .orElseThrow(() -> new ResourceNotFoundException("Player two not found"));

        Player playerThree = playerRepository.findByNickName(requestDto.getPlayerThreeNickName())
            .orElseThrow(() -> new ResourceNotFoundException("Player three not found"));

        Player playerFour = playerRepository.findByNickName(requestDto.getPlayerFourNickName())
            .orElseThrow(() -> new ResourceNotFoundException("Player four not found"));

        // Fetch teams
        Team teamOne = teamRepository.findByName(requestDto.getTeamOneName())
            .orElseThrow(() -> new ResourceNotFoundException("Team one not found"));

        Team teamTwo = teamRepository.findByName(requestDto.getTeamTwoName())
            .orElseThrow(() -> new ResourceNotFoundException("Team two not found"));

        Team teamThree = teamRepository.findByName(requestDto.getTeamThreeName())
            .orElseThrow(() -> new ResourceNotFoundException("Team three not found"));

        Team teamFour = teamRepository.findByName(requestDto.getTeamFourName())
            .orElseThrow(() -> new ResourceNotFoundException("Team four not found"));

        List<Player> players = List.of(playerOne, playerTwo, playerThree, playerFour);
        List<Team> teams = List.of(teamOne, teamTwo, teamThree, teamFour);

        // Pair each player with a team
        List<Pair<Player, Team>> playerTeamPairs = new ArrayList<>();
        for(int i = 0; i < 4; i++)
        {
            playerTeamPairs.add(Pair.of(players.get(i), teams.get(i)));
        }

        // Shuffle or manually define matchups
        Collections.shuffle(playerTeamPairs);

        Playoff playoff = new Playoff();
        playoff.setTitle(requestDto.getPlayoffTitle());

        // Create 2 semi-final series
        Series semi1 = createSeries(playerTeamPairs.get(0), playerTeamPairs.get(1), playoff);
        Series semi2 = createSeries(playerTeamPairs.get(2), playerTeamPairs.get(3), playoff);

        playoff.getSeries().addAll(List.of(semi1, semi2));
        playoff.setCreatedAt(LocalDateTime.now());
        playoffRepository.save(playoff);

        // 5. Return response
        return new PlayoffResponseDto(
            playoff.getId(),
            playoff.getCreatedAt(),
            playoff.getTitle(),
            List.of(SeriesMapper.toResponseDto(semi1), SeriesMapper.toResponseDto(semi2)),
            false,
            false,
            null,
            null,
            null);
    }

    @Override
    public List<PlayoffResponseDto> getAllPlayoffs(int limit, int offset)
    {
        List<Playoff> playoffs = playoffRepository
            .findAll(PageRequest.of(offset, limit, Sort.by("createdAt").descending()))
            .getContent();

        return playoffs.stream().map(playoff -> {
            List<SeriesResponseDto> seriesDtos = playoff.getSeries().stream()
                .map(SeriesMapper::toResponseDto)
                .toList();

            return new PlayoffResponseDto(
                playoff.getId(),
                playoff.getCreatedAt(),
                playoff.getTitle(), // maps to playoffTitle
                seriesDtos,
                playoff.isSemiFinalsCompleted(), // semiFinalsCompleted
                playoff.isFinalsCompleted(),     // finalsCompleted
                playoff.getWinner(),
                playoff.getSecondPlace(),
                playoff.getThirdPlace()
            );
        }).toList();
    }

    private Series createSeries(Pair<Player, Team> p1, Pair<Player, Team> p2, Playoff playoff)
    {
        Series series = new Series();
        series.setCreatedAt(LocalDateTime.now());
        series.setPlayerOne(p1.getFirst());
        series.setPlayerTwo(p2.getFirst());
        series.setTeamOne(p1.getSecond());
        series.setTeamTwo(p2.getSecond());
        series.setPlayoff(playoff);
        series.setWinner(null);
        series.setPlayerOneWins(0);
        series.setPlayerTwoWins(0);
        series.setCompleted(false);

        eventPublisher.publishEvent(new SeriesCreatedEvent(this, series));

        return series;
    }

    @Transactional
    public void updatePlayoffAfterSemis(Long playoffId)
    {
        Playoff playoff = playoffRepository.findById(playoffId)
            .orElseThrow(() -> new ResourceNotFoundException("Playoff not found"));

        List<Series> existingSeries = playoff.getSeries();
        if(existingSeries.size() != 2)
        {
            throw new IllegalStateException("Playoff must have exactly 2 semifinal series");
        }

        Series semi1 = existingSeries.get(0);
        Series semi2 = existingSeries.get(1);

        // Validation: both semifinals must be completed
        if(!semi1.isCompleted() || !semi2.isCompleted())
        {
            throw new IllegalStateException("Both semifinal series must be completed");
        }

        // Determine winners and losers
        Player winner1 = semi1.getWinner();
        Player loser1 = semi1.getPlayerOne().equals(winner1) ? semi1.getPlayerTwo() : semi1.getPlayerOne();

        Player winner2 = semi2.getWinner();
        Player loser2 = semi2.getPlayerOne().equals(winner2) ? semi2.getPlayerTwo() : semi2.getPlayerOne();

        // Get teams for the final and 3rd-place
        Team winner1Team = semi1.getPlayerOne().equals(winner1) ? semi1.getTeamOne() : semi1.getTeamTwo();
        Team loser1Team = semi1.getPlayerOne().equals(loser1) ? semi1.getTeamOne() : semi1.getTeamTwo();

        Team winner2Team = semi2.getPlayerOne().equals(winner2) ? semi2.getTeamOne() : semi2.getTeamTwo();
        Team loser2Team = semi2.getPlayerOne().equals(loser2) ? semi2.getTeamOne() : semi2.getTeamTwo();

        // Create final series
        Series finalSeries = createSeries(Pair.of(winner1, winner1Team), Pair.of(winner2, winner2Team), playoff);
        // Create 3rd-place series
        Series thirdPlaceSeries = createSeries(Pair.of(loser1, loser1Team), Pair.of(loser2, loser2Team), playoff);

        playoff.getSeries().addAll(List.of(finalSeries, thirdPlaceSeries));

        seriesRepository.saveAll(List.of(finalSeries, thirdPlaceSeries));
        playoffRepository.save(playoff);
    }

    public void finalizePlayoff(Long playoffId)
    {
        Playoff playoff = playoffRepository.findById(playoffId)
            .orElseThrow(() -> new ResourceNotFoundException("Playoff not found"));

        List<Series> seriesList = playoff.getSeries();
        if(seriesList.size() != 4)
        {
            throw new IllegalStateException("Playoff must have 4 series (2 semifinals, final, third-place)");
        }

        Series finalSeries = seriesList.get(2); // Assuming index order
        Series thirdPlaceSeries = seriesList.get(3);

        if(!finalSeries.isCompleted() || !thirdPlaceSeries.isCompleted())
        {
            throw new IllegalStateException("Final and 3rd-place series must be completed");
        }

        Player first = finalSeries.getWinner();
        Player second = finalSeries.getPlayerOne().equals(first)
            ? finalSeries.getPlayerTwo() : finalSeries.getPlayerOne();

        Player third = thirdPlaceSeries.getWinner();

        playoff.setWinner(first.getNickName());
        playoff.setSecondPlace(second.getNickName());
        playoff.setThirdPlace(third.getNickName());

        playoffRepository.save(playoff);
    }
}
