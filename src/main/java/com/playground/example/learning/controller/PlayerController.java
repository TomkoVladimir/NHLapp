package com.playground.example.learning.controller;

import com.playground.example.learning.dto.PlayerDto.PlayerRequestDto;
import com.playground.example.learning.dto.PlayerDto.PlayerResponseDto;
import com.playground.example.learning.service.PlayerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
