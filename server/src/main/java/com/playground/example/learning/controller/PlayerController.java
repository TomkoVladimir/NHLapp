package com.playground.example.learning.controller;

import com.playground.example.learning.code.SecretCodeValidator;
import com.playground.example.learning.dto.PlayerDto.PlayerRequestDto;
import com.playground.example.learning.dto.PlayerDto.PlayerResponseDto;
import com.playground.example.learning.dto.PlayerStatsDto;
import com.playground.example.learning.service.PlayerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/players")
@AllArgsConstructor
public class PlayerController
{
    private final PlayerService playerService;
    private final SecretCodeValidator codeValidator;

    @PostMapping
    public ResponseEntity<?> createPlayer(@RequestBody PlayerRequestDto playerRequestDto)
    {
        if (!codeValidator.isValid(playerRequestDto.getValidationCode())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Invalid code."));
        }

        PlayerResponseDto response = playerService.createPlayer(playerRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PlayerResponseDto>> getAllPlayers() {
        List<PlayerResponseDto> players = playerService.getAllPlayers();
        return ResponseEntity.ok(players);
    }
}
