package com.playground.example.learning.dto;

import com.playground.example.learning.entity.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerStatsDto
{
    private String player;
    private int matchesPlayed;
    private int wins;
    private int losses;
    private int extraTimeWins;
    private int extraTimeLosses;
    private int scoredGoals;
    private int concededGoals;
    private int points;
}
