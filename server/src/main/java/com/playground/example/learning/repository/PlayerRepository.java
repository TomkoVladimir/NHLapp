package com.playground.example.learning.repository;

import com.playground.example.learning.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long>
{
    Optional<Player> findByNickName(String nickName);
}
