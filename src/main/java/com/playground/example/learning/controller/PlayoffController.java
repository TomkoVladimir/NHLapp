package com.playground.example.learning.controller;

import com.playground.example.learning.code.SecretCodeValidator;
import com.playground.example.learning.dto.PlayoffDto.PlayoffRequestDto;
import com.playground.example.learning.dto.PlayoffDto.PlayoffResponseDto;
import com.playground.example.learning.service.PlayoffService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/playoffs")
@AllArgsConstructor
public class PlayoffController
{
    private final PlayoffService playoffService;
    private final SecretCodeValidator codeValidator;

    @PostMapping
    public ResponseEntity<?> createPlayoff(@RequestBody PlayoffRequestDto playoffRequestDto)
    {
        if (!codeValidator.isValid(playoffRequestDto.getValidationCode())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Invalid code."));
        }

        PlayoffResponseDto response = playoffService.createPlayoff(playoffRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
