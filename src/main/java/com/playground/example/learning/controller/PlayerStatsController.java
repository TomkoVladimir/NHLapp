package com.playground.example.learning.controller;

import com.playground.example.learning.dto.PlayerStatsDto;
import com.playground.example.learning.entity.Player;
import com.playground.example.learning.entity.PlayerStats;
import com.playground.example.learning.exception.ResourceNotFoundException;
import com.playground.example.learning.mapper.PlayerStatsMapper;
import com.playground.example.learning.repository.PlayerRepository;
import com.playground.example.learning.repository.PlayerStatsRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/stats")
@AllArgsConstructor
public class PlayerStatsController
{
    private final PlayerStatsRepository playerStatsRepository;
    private final PlayerRepository playerRepository;

    @GetMapping("/stats/{playerId}")
    public ResponseEntity<PlayerStatsDto> getStats(@PathVariable Long playerId) {
        Player player = playerRepository.findById(playerId)
            .orElseThrow(() -> new ResourceNotFoundException("Player not found"));

        PlayerStats stats = playerStatsRepository.findByPlayerId(player.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Player stats not found"));

        PlayerStatsDto dto = PlayerStatsMapper.toDto(stats);

        return ResponseEntity.ok(dto);
    }
}
