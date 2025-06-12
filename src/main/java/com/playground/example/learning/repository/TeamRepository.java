package com.playground.example.learning.repository;

import com.playground.example.learning.entity.Player;
import com.playground.example.learning.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long>
{
    Optional<Team> findByName(String name);
}
