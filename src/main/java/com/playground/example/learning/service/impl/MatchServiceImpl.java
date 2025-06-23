package com.playground.example.learning.service.impl;

import com.playground.example.learning.dto.MatchDto.MatchRequestDto;
import com.playground.example.learning.dto.MatchDto.MatchResponseDto;
import com.playground.example.learning.entity.*;
import com.playground.example.learning.events.MatchFinishedEvent;
import com.playground.example.learning.exception.InvalidMatchDataException;
import com.playground.example.learning.exception.ResourceNotFoundException;
import com.playground.example.learning.mapper.MatchMapper;
import com.playground.example.learning.repository.*;
import com.playground.example.learning.service.MatchesService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MatchServiceImpl implements MatchesService
{
    private MatchRepository matchRepository;
    private PlayerRepository playerRepository;
    private TeamRepository teamRepository;
    private PlayerTeamCounterRepository playerTeamCounterRepository;
    private SeriesRepository seriesRepository;
    private PlayerStatsRepository playerStatsRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public MatchResponseDto createStandaloneMatch(MatchRequestDto requestDto)
    {
        Player homePlayer = playerRepository.findByNickName(requestDto.getHtPlayerNickName())
            .orElseThrow(() -> new ResourceNotFoundException("Home player not found"));

        Player awayPlayer = playerRepository.findByNickName(requestDto.getAtPlayerNickName())
            .orElseThrow(() -> new ResourceNotFoundException("Away player not found"));

        Team homeTeam = teamRepository.findByName(requestDto.getHomeTeam())
            .orElseThrow(() -> new ResourceNotFoundException("Home team not found"));

        Team awayTeam = teamRepository.findByName(requestDto.getAwayTeam())
            .orElseThrow(() -> new ResourceNotFoundException("Away team not found"));

        PlayerStats homePlayerStats = playerStatsRepository.findByPlayerId(homePlayer.getId())
            .orElseGet(() -> {
                PlayerStats newStats = new PlayerStats();
                newStats.setPlayer(homePlayer);
                return playerStatsRepository.save(newStats);
            });

        PlayerStats awayPlayerStats = playerStatsRepository.findByPlayerId(awayPlayer.getId())
            .orElseGet(() -> {
                PlayerStats newStats = new PlayerStats();
                newStats.setPlayer(awayPlayer);
                return playerStatsRepository.save(newStats);
            });

        homePlayerStats.setMatchesPlayed(homePlayerStats.getMatchesPlayed() + 1);
        awayPlayerStats.setMatchesPlayed(awayPlayerStats.getMatchesPlayed() + 1);
        homePlayerStats.setScoredGoals(homePlayerStats.getScoredGoals() + requestDto.getHtScore());
        homePlayerStats.setConcededGoals(homePlayerStats.getConcededGoals() + requestDto.getAtScore());
        awayPlayerStats.setScoredGoals(awayPlayerStats.getScoredGoals() + requestDto.getAtScore());
        awayPlayerStats.setConcededGoals(awayPlayerStats.getConcededGoals() + requestDto.getHtScore());

        updateTeamUsage(homePlayer, homeTeam);
        updateTeamUsage(awayPlayer, awayTeam);

        // Use mapper to set basic fields (except Player objects)
        Match match = MatchMapper.toMatchEntity(requestDto);

        // Now set the Player entities explicitly
        match.setHtPlayer(homePlayer);
        match.setAtPlayer(awayPlayer);
        match.setHomeTeam(homeTeam);
        match.setAwayTeam(awayTeam);

        // Optional: Set series
        if(requestDto.getSeriesId() != null)
        {
            Series series = seriesRepository.findById(requestDto.getSeriesId())
                .orElseThrow(() -> new ResourceNotFoundException("Series not found"));
            match.setSeries(series);
        }
        else
        {
            match.setSeries(null); // standalone match
        }

        // Calculate points
        int homePoints = 0;
        int awayPoints = 0;
        if(requestDto.getOverTime())
        {
            int diff = Math.abs(requestDto.getHtScore() - requestDto.getAtScore());
            if(diff != 1)
            {
                throw new InvalidMatchDataException("Overtime is only allowed with a 1-goal difference.");
            }
            else if(requestDto.getHtScore() > requestDto.getAtScore())
            {
                homePoints = 2;
                homePlayerStats.setExtraTimeWins(homePlayerStats.getExtraTimeWins() + 1);
                awayPoints = 1;
                awayPlayerStats.setExtraTimeLosses(awayPlayerStats.getExtraTimeLosses() + 1);
            }
            else
            {
                homePoints = 1;
                homePlayerStats.setExtraTimeLosses(homePlayerStats.getExtraTimeLosses() + 1);
                awayPoints = 2;
                awayPlayerStats.setExtraTimeWins(awayPlayerStats.getExtraTimeWins() + 1);
            }
        }
        else if(requestDto.getHtScore() > requestDto.getAtScore())
        {
            homePoints = 3;
            homePlayerStats.setWins(homePlayerStats.getWins() + 1);
            awayPlayerStats.setLosses(awayPlayerStats.getLosses() + 1);
        }
        else
        {
            awayPoints = 3;
            awayPlayerStats.setWins(awayPlayerStats.getWins() + 1);
            homePlayerStats.setLosses(homePlayerStats.getLosses() + 1);
        }

        match.setHtPoints(homePoints);
        match.setAtPoints(awayPoints);

        homePlayerStats.setPoints(homePlayerStats.getPoints() + homePoints);
        awayPlayerStats.setPoints(awayPlayerStats.getPoints() + awayPoints);

        match.setIsFinished(true); // Assuming match is finished upon creation

        // Save
        Match savedMatch = matchRepository.save(match);
        playerStatsRepository.save(homePlayerStats);
        playerStatsRepository.save(awayPlayerStats);

        // Return DTO
        return MatchMapper.toResponseDto(savedMatch);
    }

    public Match createSeriesMatch(Player homePlayer, Player awayPlayer, Team homeTeam, Team awayTeam, Series series)
    {
        Match match = new Match();
        match.setMatchDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))); // Set current date
        match.setHtPlayer(homePlayer);
        match.setAtPlayer(awayPlayer);
        match.setHomeTeam(homeTeam);
        match.setAwayTeam(awayTeam);
        match.setSeries(series);
        match.setIsFinished(false); // Initially not finished
        return matchRepository.save(match);
    }

    @Override
    public MatchResponseDto finishMatch(Long matchId, int htScore, int atScore, boolean overTime)
    {
        Match match = matchRepository.findById(matchId)
            .orElseThrow(() -> new ResourceNotFoundException("Match not found"));

        PlayerStats homePlayerStats = playerStatsRepository.findByPlayerNickName(match.getHtPlayer().getNickName())
            .orElseGet(() -> {
                PlayerStats newStats = new PlayerStats();
                newStats.setPlayer(match.getHtPlayer());
                return playerStatsRepository.save(newStats);
            });

        PlayerStats awayPlayerStats = playerStatsRepository.findByPlayerNickName(match.getAtPlayer().getNickName())
            .orElseGet(() -> {
                PlayerStats newStats = new PlayerStats();
                newStats.setPlayer(match.getAtPlayer());
                return playerStatsRepository.save(newStats);
            });

        if(match.getIsFinished())
        {
            throw new InvalidMatchDataException("Match is already finished.");
        }

        homePlayerStats.setMatchesPlayed(homePlayerStats.getMatchesPlayed() + 1);
        awayPlayerStats.setMatchesPlayed(awayPlayerStats.getMatchesPlayed() + 1);
        homePlayerStats.setScoredGoals(homePlayerStats.getScoredGoals() + htScore);
        homePlayerStats.setConcededGoals(homePlayerStats.getConcededGoals() + atScore);
        awayPlayerStats.setScoredGoals(awayPlayerStats.getScoredGoals() + atScore);
        awayPlayerStats.setConcededGoals(awayPlayerStats.getConcededGoals() + htScore);

        match.setHtScore(htScore);
        match.setAtScore(atScore);
        match.setOverTime(overTime);

        // Calculate points
        int homePoints = 0;
        int awayPoints = 0;
        if(overTime)
        {
            int diff = Math.abs(htScore - atScore);
            if(diff != 1) throw new InvalidMatchDataException("OT must be decided by 1 goal.");
            if(htScore > atScore)
            {
                homePoints = 2;
                homePlayerStats.setExtraTimeWins(homePlayerStats.getExtraTimeWins() + 1);
                awayPoints = 1;
                awayPlayerStats.setExtraTimeLosses(awayPlayerStats.getExtraTimeLosses() + 1);
            }
            else
            {
                homePoints = 1;
                homePlayerStats.setExtraTimeLosses(homePlayerStats.getExtraTimeLosses() + 1);
                awayPoints = 2;
                awayPlayerStats.setExtraTimeWins(awayPlayerStats.getExtraTimeWins() + 1);
            }
        }
        else if(htScore > atScore)
        {
            homePoints = 3;
            homePlayerStats.setWins(homePlayerStats.getWins() + 1);
            awayPlayerStats.setLosses(awayPlayerStats.getLosses() + 1);
        }
        else
        {
            awayPoints = 3;
            awayPlayerStats.setWins(awayPlayerStats.getWins() + 1);
            homePlayerStats.setLosses(homePlayerStats.getLosses() + 1);
        }

        match.setHtPoints(homePoints);
        match.setAtPoints(awayPoints);
        homePlayerStats.setPoints(homePlayerStats.getPoints() + homePoints);
        awayPlayerStats.setPoints(awayPlayerStats.getPoints() + awayPoints);
        match.setIsFinished(true);

        updateTeamUsage(match.getHtPlayer(), match.getHomeTeam());
        updateTeamUsage(match.getAtPlayer(), match.getAwayTeam());

        matchRepository.save(match);
        playerStatsRepository.save(homePlayerStats);
        playerStatsRepository.save(awayPlayerStats);

        eventPublisher.publishEvent(new MatchFinishedEvent(this, match));

        return MatchMapper.toResponseDto(match);
    }

    public List<MatchResponseDto> getAllMatches(int limit, int offset) {
        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.DESC, "matchDate"));

        return matchRepository.findAll(pageable)
            .stream()
            .map(MatchMapper::toResponseDto)
            .collect(Collectors.toList());
    }

    private void updateTeamUsage(Player player, Team team)
    {
        Optional<PlayerTeamCounter> existingPairPlayerAndTeam = playerTeamCounterRepository.findByPlayerAndTeam(player, team);

        if(existingPairPlayerAndTeam.isPresent())
        {
            PlayerTeamCounter counter = existingPairPlayerAndTeam.get();
            counter.setMatchCount(counter.getMatchCount() + 1);
            playerTeamCounterRepository.save(counter);
        }
        else
        {
            playerTeamCounterRepository.save(new PlayerTeamCounter(player, team));
        }
    }

    @Override
    public MatchResponseDto getMatchById(Long matchId)
    {
        Match match = matchRepository.findById(matchId)
            .orElseThrow(() -> new ResourceNotFoundException("Match not found"));

        return MatchMapper.toResponseDto(match);
    }
}
