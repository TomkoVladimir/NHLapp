package com.playground.example.learning.controller;

import com.playground.example.learning.dto.PlayerDto.PlayerRequestDto;
import com.playground.example.learning.dto.PlayerDto.PlayerResponseDto;
import com.playground.example.learning.dto.PlayerDto.PlayerStatsDto;
import com.playground.example.learning.service.PlayerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/players")
@AllArgsConstructor
public class PlayerController
{
    private final PlayerService playerService;

    @PostMapping
    public ResponseEntity<PlayerResponseDto> createPlayer(@RequestBody PlayerRequestDto playerRequestDto) {
        PlayerResponseDto response = playerService.createPlayer(playerRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{playerId}/stats")
    public ResponseEntity<PlayerStatsDto> getPlayerStats(@PathVariable("playerId") Long playerId) {
        PlayerStatsDto stats = playerService.getPlayerStats(playerId);
        return ResponseEntity.ok(stats);
    }
}
