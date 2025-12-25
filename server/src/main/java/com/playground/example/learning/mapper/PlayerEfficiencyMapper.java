package com.playground.example.learning.mapper;

import com.playground.example.learning.dto.PlayerEfficiencyDto;
import com.playground.example.learning.entity.PlayerStats;

public class PlayerEfficiencyMapper
{
    public static PlayerEfficiencyDto toDto(PlayerStats stats) {
        PlayerEfficiencyDto dto = new PlayerEfficiencyDto();
        dto.setPlayerId(stats.getPlayer().getId());
        dto.setPlayerName(stats.getPlayer().getNickName());

        // Calculate efficiency as Points / Matches Played
        double efficiency = stats.getPoints() / (double) stats.getMatchesPlayed();
        dto.setEfficiency(efficiency);

        return dto;
    }
}
