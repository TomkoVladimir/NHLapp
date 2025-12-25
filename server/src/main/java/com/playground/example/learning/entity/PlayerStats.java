package com.playground.example.learning.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "player_stats")
public class PlayerStats
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Player player;

    private int matchesPlayed;
    private int wins;
    private int losses;
    private int extraTimeWins;
    private int extraTimeLosses;
    private int scoredGoals;
    private int concededGoals;
    private int points;
}
