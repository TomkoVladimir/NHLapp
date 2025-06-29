package com.playground.example.learning.mapper;

import com.playground.example.learning.dto.MatchDto.MatchRequestDto;
import com.playground.example.learning.dto.MatchDto.MatchResponseDto;
import com.playground.example.learning.entity.Match;

public class MatchMapper
{
    // Map from DTO to Entity
    public static Match toMatchEntity(MatchRequestDto dto)
    {
        Match match = new Match();
        match.setMatchDate(dto.getMatchDate());
        match.setHtScore(dto.getHtScore());
        match.setAtScore(dto.getAtScore());
        match.setOverTime(dto.getOverTime());
        return match;
    }

    // Map from Entity to Response DTO
    public static MatchResponseDto toResponseDto(Match match)
    {
        MatchResponseDto dto = new MatchResponseDto();
        dto.setId(match.getId());
        dto.setMatchDate(match.getMatchDate());
        dto.setHtPlayerNickName(match.getHtPlayer().getNickName());
        dto.setAtPlayerNickName(match.getAtPlayer().getNickName());
        dto.setHomeTeam(match.getHomeTeam());
        dto.setAwayTeam(match.getAwayTeam());
        dto.setHtScore(match.getHtScore());
        dto.setAtScore(match.getAtScore());
        dto.setOverTime(match.getOverTime());
        dto.setHtPoints(match.getHtPoints());
        dto.setAtPoints(match.getAtPoints());
        dto.setSeriesId(Math.toIntExact(match.getSeries() != null ? match.getSeries().getId() : 0L));
        return dto;
    }
}
