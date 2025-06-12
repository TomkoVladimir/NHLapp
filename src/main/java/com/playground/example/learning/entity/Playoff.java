package com.playground.example.learning.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
