package com.playground.example.learning.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerEfficiencyDto
{
    private Long playerId;
    private String playerName;
    private double efficiency; // Efficiency is calculated as Points / Matches Played
}
