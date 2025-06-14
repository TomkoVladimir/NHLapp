package com.playground.example.learning.controller;

import com.playground.example.learning.dto.PlayerDto.PlayerRequestDto;
import com.playground.example.learning.dto.PlayerDto.PlayerResponseDto;
import com.playground.example.learning.dto.PlayoffDto.PlayoffRequestDto;
import com.playground.example.learning.dto.PlayoffDto.PlayoffResponseDto;
import com.playground.example.learning.service.PlayerService;
import com.playground.example.learning.service.PlayoffService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/playoffs")
@AllArgsConstructor
public class PlayoffController
{
    private final PlayoffService playoffService;

    @PostMapping
    public ResponseEntity<PlayoffResponseDto> createPlayoff(@RequestBody PlayoffRequestDto playoffRequestDto) {
        PlayoffResponseDto response = playoffService.createPlayoff(playoffRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
