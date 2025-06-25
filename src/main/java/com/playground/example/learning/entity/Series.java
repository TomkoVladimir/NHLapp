package com.playground.example.learning.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "series")
public class Series
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "player_one_id")
    private Player playerOne;

    @ManyToOne(optional = false)
    @JoinColumn(name = "player_two_id")
    private Player playerTwo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "team_one_id")
    private Team teamOne;

    @ManyToOne(optional = false)
    @JoinColumn(name = "team_two_id")
    private Team teamTwo;

    @OneToMany(mappedBy = "series", cascade = CascadeType.ALL)
    private List<Match> matches = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "playoff_id")
    private Playoff playoff; // optional

    @ManyToOne
    @JoinColumn(name = "winner_id")
    private Player winner;

    @Column(name = "player_one_wins")
    private int playerOneWins;

    @Column(name = "player_two_wins")
    private int playerTwoWins;

    @Column(name = "completed", nullable = false)
    private boolean completed = false;
}


