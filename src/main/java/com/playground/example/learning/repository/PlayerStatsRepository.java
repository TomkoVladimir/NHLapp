package com.playground.example.learning.repository;

import com.playground.example.learning.entity.Player;
import com.playground.example.learning.entity.PlayerStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlayerStatsRepository extends JpaRepository<PlayerStats, Long>
{
    @Query("SELECT ps FROM PlayerStats ps WHERE ps.player.id = :playerId")
    Optional<PlayerStats> findByPlayerId(@Param("playerId") Long playerId);

    Optional<PlayerStats> findByPlayerNickName(String playerNickName);

    Optional<PlayerStats> findByPlayer(Player player);
}
