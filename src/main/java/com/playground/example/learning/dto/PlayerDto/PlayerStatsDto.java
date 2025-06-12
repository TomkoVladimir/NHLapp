package com.playground.example.learning.dto.PlayerDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PlayerStatsDto
{
    private Long playerId;
    private String nickName;
    private String favouriteTeam;
    private int totalGamesPlayed;
    private int totalPoints;
    private int totalWins;
    private int totalLosses;
    private int totalOverTimes;
    private String homeStatistics;
    private String awayStatistics;
    private  int totalGoalsScored;
    private int totalGoalsConceded;
    private int goalDifference;
    private double winRate;
    private int scoredGoalsPerGame;
    private int concededGoalsPerGame;
}
