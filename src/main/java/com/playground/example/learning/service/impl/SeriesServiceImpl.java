package com.playground.example.learning.service.impl;

import com.playground.example.learning.dto.SeriesDto.SeriesRequestDto;
import com.playground.example.learning.dto.SeriesDto.SeriesResponseDto;
import com.playground.example.learning.entity.Match;
import com.playground.example.learning.entity.Player;
import com.playground.example.learning.entity.Series;
import com.playground.example.learning.entity.Team;
import com.playground.example.learning.exception.ResourceNotFoundException;
import com.playground.example.learning.mapper.SeriesMapper;
import com.playground.example.learning.repository.MatchRepository;
import com.playground.example.learning.repository.PlayerRepository;
import com.playground.example.learning.repository.SeriesRepository;
import com.playground.example.learning.repository.TeamRepository;
import com.playground.example.learning.service.SeriesService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SeriesServiceImpl implements SeriesService
{
    private final SeriesRepository seriesRepository;
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;
    private final MatchServiceImpl matchService;

    @Override
    public SeriesResponseDto createSeries(SeriesRequestDto dto)
    {
        // Fetch players
        Player playerOne = playerRepository.findByNickName(dto.getPlayerOneNickName())
            .orElseThrow(() -> new ResourceNotFoundException("Player one not found"));

        Player playerTwo = playerRepository.findByNickName(dto.getPlayerTwoNickName())
            .orElseThrow(() -> new ResourceNotFoundException("Player two not found"));

        // Fetch teams
        Team teamOne = teamRepository.findByName(dto.getTeamOneName())
            .orElseThrow(() -> new ResourceNotFoundException("Team one not found"));

        Team teamTwo = teamRepository.findByName(dto.getTeamTwoName())
            .orElseThrow(() -> new ResourceNotFoundException("Team two not found"));

        // Create Series entity
        Series series = new Series();
        series.setPlayerOne(playerOne);
        series.setPlayerTwo(playerTwo);
        series.setTeamOne(teamOne);
        series.setTeamTwo(teamTwo);
        series.setCompleted(false);
        seriesRepository.save(series);

        // Create initial 4 matches (2 home for each player)
        List<Match> matches = new ArrayList<>();

        // Game 1 & 2: playerOne home
        matches.add(matchService.createSeriesMatch(playerOne, playerTwo, teamOne, teamTwo, series));
        matches.add(matchService.createSeriesMatch(playerOne, playerTwo, teamOne, teamTwo, series));

        // Game 3 & 4: playerTwo home
        matches.add(matchService.createSeriesMatch(playerTwo, playerOne, teamTwo, teamOne, series));
        matches.add(matchService.createSeriesMatch(playerTwo, playerOne, teamTwo, teamOne, series));

        matchRepository.saveAll(matches);

        // Return response
        List<Long> matchIds = matches.stream().map(Match::getId).collect(Collectors.toList());

        return new SeriesResponseDto(
                    series.getId(),
                    playerOne.getNickName(),
                    playerTwo.getNickName(),
                    teamOne.getName(),
                    teamTwo.getName(),
                    null,
                    series.getPlayoff() != null ? series.getPlayoff().getId() : null,
                    matchIds, // Optional playoff ID
                    series.isCompleted()
                );
    }

    public void updateSeriesAfterMatch(Match match)
    {
        Series series = match.getSeries();
        if (series == null || series.isCompleted() || !match.getIsFinished()) return;

        int htScore = match.getHtScore();
        int atScore = match.getAtScore();

        Player homePlayer = match.getHtPlayerNickName();
        Player awayPlayer = match.getAtPlayerNickName();

        if (htScore == atScore) {
            // Invalid NHL scenario: a playoff game can't end in a tie
            throw new IllegalArgumentException("Match cannot end in a draw in a playoff series.");
        }

        // Determine winner and update series win count
        Player winner = (htScore > atScore) ? homePlayer : awayPlayer;
        if (winner.equals(series.getPlayerOne())) {
            series.setPlayerOneWins(series.getPlayerOneWins() + 1);
        } else if (winner.equals(series.getPlayerTwo())) {
            series.setPlayerTwoWins(series.getPlayerTwoWins() + 1);
        }

        // Check if the series is completed
        if (series.getPlayerOneWins() == 4 || series.getPlayerTwoWins() == 4) {
            series.setCompleted(true);
            if (series.getPlayerOneWins() == 4) {
                series.setWinner(series.getPlayerOne());
            } else {
                series.setWinner(series.getPlayerTwo());
            }
            seriesRepository.save(series);
            return;
        }

        // Count current matches
        int totalMatches = matchRepository.countBySeries(series);
        if (totalMatches >= 7) return; // Defensive check, though it shouldn't reach here

        // Schedule next match in the series
        if(series.getPlayerOneWins() == series.getPlayerTwoWins()){
            Match nextMatch = createNextSeriesMatch(series, totalMatches + 1);
            matchRepository.save(nextMatch);
        }
        if((series.getPlayerOneWins() != 0 && series.getPlayerTwoWins() != 0) && (series.getPlayerOneWins() + series.getPlayerTwoWins() == 3 ))
        {
            Match nextMatch = createNextSeriesMatch(series, totalMatches + 1);
            matchRepository.save(nextMatch);
        }
        if((series.getPlayerOneWins() != 0 && series.getPlayerTwoWins() != 0) && (series.getPlayerOneWins() + series.getPlayerTwoWins() == 4 ))
        {
            Match nextMatch = createNextSeriesMatch(series, totalMatches + 1);
            matchRepository.save(nextMatch);
        }
        if((series.getPlayerOneWins() != 0 && series.getPlayerTwoWins() != 0) && (series.getPlayerOneWins() + series.getPlayerTwoWins() == 5 ))
        {
            Match nextMatch = createNextSeriesMatch(series, totalMatches + 1);
            matchRepository.save(nextMatch);
        }

        // Save the updated series just in case
        seriesRepository.save(series);
    }

    @Override
    public Match createNextSeriesMatch(Series series, int matchNumber)
    {
        Player homePlayer;
        Player awayPlayer;
        Team homeTeam;
        Team awayTeam;

        switch(matchNumber)
        {
            case 5:
            case 7:
                homePlayer = series.getPlayerOne();
                awayPlayer = series.getPlayerTwo();
                homeTeam = series.getTeamOne();
                awayTeam = series.getTeamTwo();
                break;
            case 6:
                homePlayer = series.getPlayerTwo();
                awayPlayer = series.getPlayerOne();
                homeTeam = series.getTeamTwo();
                awayTeam = series.getTeamOne();
                break;
            default:
                throw new IllegalArgumentException("Invalid match number for extended series");
        }

        return matchService.createSeriesMatch(homePlayer, awayPlayer, homeTeam, awayTeam, series);
    }

    public List<SeriesResponseDto> getAllSeries() {
        List<Series> seriesList = seriesRepository.findAll();
        return seriesList.stream()
            .map(SeriesMapper::toResponseDto)
            .collect(Collectors.toList());
    }
}
