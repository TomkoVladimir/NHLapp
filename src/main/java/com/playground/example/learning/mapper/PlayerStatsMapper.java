package com.playground.example.learning.mapper;

import com.playground.example.learning.dto.PlayerStatsDto;
import com.playground.example.learning.entity.PlayerStats;

public class PlayerStatsMapper
{
    public static PlayerStatsDto toDto(PlayerStats stats) {
        PlayerStatsDto dto = new PlayerStatsDto();
        dto.setPlayer(stats.getPlayer().getNickName()); // Or .getName(), depending on what you want
        dto.setMatchesPlayed(stats.getMatchesPlayed());
        dto.setWins(stats.getWins());
        dto.setLosses(stats.getLosses());
        dto.setExtraTimeWins(stats.getExtraTimeWins());
        dto.setExtraTimeLosses(stats.getExtraTimeLosses());
        dto.setScoredGoals(stats.getScoredGoals());
        dto.setConcededGoals(stats.getConcededGoals());
        dto.setPoints(stats.getPoints());
        return dto;
    }
}
