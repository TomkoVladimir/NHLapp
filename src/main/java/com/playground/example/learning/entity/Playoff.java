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
@Table(name = "playoffs")
public class Playoff
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "title", nullable = false)
    private String title;

    @OneToMany(mappedBy = "playoff", cascade = CascadeType.ALL)
    private List<Series> series = new ArrayList<>();

    @Column(name = "semi_finals_completed", nullable = false)
    private boolean semiFinalsCompleted = false;

    @Column(name = "finals_completed", nullable = false)
    private boolean finalsCompleted = false;

    @Column(name = "winner")
    private String winner;

    @Column(name = "second_place")
    private String secondPlace;

    @Column(name = "third_place")
    private String thirdPlace;
}
