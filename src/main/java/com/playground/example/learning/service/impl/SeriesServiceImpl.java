package com.playground.example.learning.service.impl;

import com.playground.example.learning.dto.SeriesDto.SeriesRequestDto;
import com.playground.example.learning.dto.SeriesDto.SeriesResponseDto;
import com.playground.example.learning.entity.*;
import com.playground.example.learning.exception.ResourceNotFoundException;
import com.playground.example.learning.mapper.SeriesMapper;
import com.playground.example.learning.repository.*;
import com.playground.example.learning.service.SeriesService;
import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
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
    private final PlayoffServiceImpl playoffService;
    private final PlayoffRepository playoffRepository;

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
        List<Match> matches = generateInitialMatchesForSeries(series);

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

    public List<Match> generateInitialMatchesForSeries(Series series) {
        Player p1 = series.getPlayerOne();
        Player p2 = series.getPlayerTwo();
        Team t1 = series.getTeamOne();
        Team t2 = series.getTeamTwo();

        List<Match> matches = new ArrayList<>();
        // Game 1 & 2: p1 home
        matches.add(matchService.createSeriesMatch(p1, p2, t1, t2, series));
        matches.add(matchService.createSeriesMatch(p1, p2, t1, t2, series));
        // Game 3 & 4: p2 home
        matches.add(matchService.createSeriesMatch(p2, p1, t2, t1, series));
        matches.add(matchService.createSeriesMatch(p2, p1, t2, t1, series));

        matchRepository.saveAll(matches);
        return matches;
    }

    public void updateSeriesAfterMatch(Match match)
    {
        Series series = match.getSeries();
        if (series == null || series.isCompleted() || !match.getIsFinished()) return;

        int htScore = match.getHtScore();
        int atScore = match.getAtScore();

        Player homePlayer = match.getHtPlayer();
        Player awayPlayer = match.getAtPlayer();

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
        }

        // Count current matches
        int totalMatches = matchRepository.countBySeries(series);
        if (totalMatches >= 7) return; // Defensive check, though it shouldn't reach here

        // Schedule next match in the series
        if(series.getPlayerOneWins() == 1 && series.getPlayerTwoWins() == 1){
            Match nextMatch = createNextSeriesMatch(series, totalMatches + 1);
            matchRepository.save(nextMatch);
        }
        if((series.getPlayerOneWins() != 0 && series.getPlayerTwoWins() != 0) &&
           (series.getPlayerOneWins() + series.getPlayerTwoWins() == 3 ) &&
           totalMatches < 5)
        {
            Match nextMatch = createNextSeriesMatch(series, totalMatches + 1);
            matchRepository.save(nextMatch);
        }
        if(series.getPlayerOneWins() == 2 && series.getPlayerTwoWins() == 2){
            Match nextMatch = createNextSeriesMatch(series, totalMatches + 1);
            matchRepository.save(nextMatch);
        }
        if((series.getPlayerOneWins() != 0 && series.getPlayerTwoWins() != 0) &&
           (series.getPlayerOneWins() + series.getPlayerTwoWins() == 4 )&&
           totalMatches < 5)
        {
            Match nextMatch = createNextSeriesMatch(series, totalMatches + 1);
            matchRepository.save(nextMatch);
        }
        if((series.getPlayerOneWins() > 1 && series.getPlayerTwoWins() > 1) &&
           (series.getPlayerOneWins() + series.getPlayerTwoWins() == 5 ) &&
           totalMatches < 6)
        {
            Match nextMatch = createNextSeriesMatch(series, totalMatches + 1);
            matchRepository.save(nextMatch);
        }
        if(series.getPlayerOneWins() == 3 && series.getPlayerTwoWins() == 3){
            Match nextMatch = createNextSeriesMatch(series, totalMatches + 1);
            matchRepository.save(nextMatch);
        }

        // Save the updated series just in case
        seriesRepository.save(series);

        // After setting series as completed and saving it
        if (series.getPlayoff() != null) {
            Playoff playoff = playoffRepository.findByIdWithSeries(series.getPlayoff().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Playoff not found"));
            List<Series> allSeries = playoff.getSeries();

            // If it's still in semifinals stage
            if (allSeries.size() == 2 && allSeries.get(0).isCompleted() && allSeries.get(1).isCompleted()) {
                playoffService.updatePlayoffAfterSemis(playoff.getId());
            }

            // If already includes final + 3rd place, check if all 4 are done
            if (allSeries.size() == 4 && allSeries.get(2).isCompleted() && allSeries.get(3).isCompleted()) {
                playoffService.finalizePlayoff(playoff.getId());
            }
        }
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
