package com.playground.example.learning.repository;

import com.playground.example.learning.entity.PlayerStats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerStatsRepository extends JpaRepository<PlayerStats, Long>
{
    Optional<PlayerStats> findByPlayerId(Long playerId);

    Optional<PlayerStats> findByPlayerNickName(String playerNickName);
}
