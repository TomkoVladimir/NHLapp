package com.playground.example.learning.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "playoff", cascade = CascadeType.ALL)
    private List<Series> series = new ArrayList<>();

    @Column(name = "winner")
    private String winner;

    @Column(name = "second_place")
    private String secondPlace;

    @Column(name = "third_place")
    private String thirdPlace;
}
