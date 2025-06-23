package com.playground.example.learning.repository;

import com.playground.example.learning.entity.Player;
import com.playground.example.learning.entity.PlayerTeamCounter;
import com.playground.example.learning.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PlayerTeamCounterRepository extends JpaRepository<PlayerTeamCounter, Long>
{
    Optional<PlayerTeamCounter> findByPlayerAndTeam(Player player, Team team);

    List<PlayerTeamCounter> findAllByPlayer(Player player);

    @Query("""
        SELECT pc.player AS player,
               pc.team AS team,
               pc.matchCount AS maxCount
        FROM PlayerTeamCounter pc
        WHERE pc.matchCount = (
            SELECT MAX(pc2.matchCount)
            FROM PlayerTeamCounter pc2
            WHERE pc2.player = pc.player
        )
    """)
    List<Object[]> findFavoriteTeamForEachPlayer();
}
