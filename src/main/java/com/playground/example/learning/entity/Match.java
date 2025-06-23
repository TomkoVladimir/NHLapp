package com.playground.example.learning.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "matches")
public class Match
{
    @SequenceGenerator(
        name = "match_seq",
        sequenceName = "match_seq", // Name of DB sequence
        allocationSize = 1
    )
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "match_seq")
    private Long id;

    @Column(name = "match_date", nullable = false)
    private String matchDate;

    @ManyToOne
    @JoinColumn(name = "ht_player_nickname", nullable = false)
    private Player htPlayer;

    @ManyToOne
    @JoinColumn(name = "at_player_nickname", nullable = false)
    private Player atPlayer;

    @ManyToOne
    @JoinColumn(name = "home_team", nullable = false)
    private Team homeTeam;

    @ManyToOne
    @JoinColumn(name = "away_team", nullable = false)
    private Team awayTeam;

    @Column(name = "ht_score")
    private Integer htScore;

    @Column(name = "at_score")
    private Integer atScore;

    @Column(name = "over_time")
    private Boolean overTime;

    @Column(name = "ht_points")
    private Integer htPoints;

    @Column(name = "at_points")
    private Integer atPoints;

    @ManyToOne
    @JoinColumn(name = "series_id")
    private Series series;

    @Column
    private Boolean isFinished;
}

