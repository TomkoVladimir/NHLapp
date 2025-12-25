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
@Table(name = "player_team_counter")
public class PlayerTeamCounter
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(optional = false)
    private Player player;

    @ManyToOne(optional = false)
    private Team team;

    @Column(nullable = false)
    private int matchCount;

    public PlayerTeamCounter(Player player, Team team) {
        this.player = player;
        this.team = team;
        this.matchCount = 1;
    }
}
