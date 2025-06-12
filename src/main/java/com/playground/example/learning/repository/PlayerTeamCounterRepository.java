package com.playground.example.learning.repository;

import com.playground.example.learning.entity.Player;
import com.playground.example.learning.entity.PlayerTeamCounter;
import com.playground.example.learning.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerTeamCounterRepository extends JpaRepository<PlayerTeamCounter, Long>
{
    Optional<PlayerTeamCounter> findByPlayerAndTeam(Player player, Team team);
}
