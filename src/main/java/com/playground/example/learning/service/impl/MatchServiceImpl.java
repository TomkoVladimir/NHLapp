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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public MatchResponseDto createStandaloneMatch(MatchRequestDto requestDto)
    {
        // 1. Find players by nickname
        Player homePlayer = playerRepository.findByNickName(requestDto.getHtPlayerNickName())
            .orElseThrow(() -> new ResourceNotFoundException("Home player not found"));

        Player awayPlayer = playerRepository.findByNickName(requestDto.getAtPlayerNickName())
            .orElseThrow(() -> new ResourceNotFoundException("Away player not found"));

        Team homeTeam = teamRepository.findByName(requestDto.getHomeTeam())
            .orElseThrow(() -> new ResourceNotFoundException("Home team not found"));

        Team awayTeam = teamRepository.findByName(requestDto.getAwayTeam())
            .orElseThrow(() -> new ResourceNotFoundException("Home team not found"));

        updateTeamUsage(homePlayer, homeTeam);
        updateTeamUsage(awayPlayer, awayTeam);

        // Use mapper to set basic fields (except Player objects)
        Match match = MatchMapper.toMatchEntity(requestDto);

        // Now set the Player entities explicitly
        match.setHtPlayerNickName(homePlayer);
        match.setAtPlayerNickName(awayPlayer);
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
                awayPoints = 1;
            }
            else
            {
                homePoints = 1;
                awayPoints = 2;
            }
        }
        else if(requestDto.getHtScore() > requestDto.getAtScore())
        {
            homePoints = 3;
        }
        else
        {
            awayPoints = 3;
        }

        match.setHtPoints(homePoints);
        match.setAtPoints(awayPoints);

        match.setIsFinished(true); // Assuming match is finished upon creation

        // Save
        Match savedMatch = matchRepository.save(match);

        // Return DTO
        return MatchMapper.toResponseDto(savedMatch);
    }

    public Match createSeriesMatch(Player homePlayer, Player awayPlayer, Team homeTeam, Team awayTeam, Series series)
    {
        Match match = new Match();
        match.setMatchDate(LocalDateTime.now().toString()); // Set current date
        match.setHtPlayerNickName(homePlayer);
        match.setAtPlayerNickName(awayPlayer);
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

        if(match.getIsFinished())
        {
            throw new InvalidMatchDataException("Match is already finished.");
        }

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
                awayPoints = 1;
            }
            else
            {
                homePoints = 1;
                awayPoints = 2;
            }
        }
        else if(htScore > atScore)
        {
            homePoints = 3;
        }
        else
        {
            awayPoints = 3;
        }

        match.setHtPoints(homePoints);
        match.setAtPoints(awayPoints);
        match.setIsFinished(true);

        matchRepository.save(match);

        eventPublisher.publishEvent(new MatchFinishedEvent(this, match));

        return MatchMapper.toResponseDto(match);
    }

    public List<MatchResponseDto> getAllMatches()
    {
        return matchRepository.findAll().stream()
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
}
