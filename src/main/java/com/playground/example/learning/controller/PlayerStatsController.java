package com.playground.example.learning.controller;

import com.playground.example.learning.dto.PlayerEfficiencyDto;
import com.playground.example.learning.dto.PlayerStatsDto;
import com.playground.example.learning.entity.Player;
import com.playground.example.learning.entity.PlayerStats;
import com.playground.example.learning.exception.ResourceNotFoundException;
import com.playground.example.learning.mapper.PlayerStatsMapper;
import com.playground.example.learning.repository.PlayerRepository;
import com.playground.example.learning.repository.PlayerStatsRepository;
import com.playground.example.learning.service.PlayerStatsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/stats")
@AllArgsConstructor
public class PlayerStatsController
{
    private final PlayerStatsRepository playerStatsRepository;
    private final PlayerRepository playerRepository;
    private final PlayerStatsService playerStatsService;

    @GetMapping
    public ResponseEntity<List<PlayerStatsDto>> getAllStats()
    {
        List<PlayerStats> statsList = playerStatsRepository.findAll();
        List<PlayerStatsDto> dtoList = statsList.stream()
            .map(PlayerStatsMapper::toDto)
            .toList();

        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<PlayerStatsDto> getStats(@PathVariable("playerId") Long playerId)
    {
        Player player = playerRepository.findById(playerId)
            .orElseThrow(() -> new ResourceNotFoundException("Player not found"));

        PlayerStats stats = playerStatsRepository.findByPlayer(player)
            .orElseThrow(() -> new ResourceNotFoundException("Player stats not found"));

        PlayerStatsDto dto = PlayerStatsMapper.toDto(stats);

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/efficiency")
    public ResponseEntity<List<PlayerEfficiencyDto>> getAllEfficiencies()
    {
        return ResponseEntity.ok(playerStatsService.getAllEfficiencies());
    }
}
