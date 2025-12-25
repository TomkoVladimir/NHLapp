package com.playground.example.learning.controller;

import com.playground.example.learning.code.SecretCodeValidator;
import com.playground.example.learning.dto.PlayoffDto.PlayoffRequestDto;
import com.playground.example.learning.dto.PlayoffDto.PlayoffResponseDto;
import com.playground.example.learning.entity.Playoff;
import com.playground.example.learning.repository.PlayoffRepository;
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
    private final PlayoffRepository playoffRepository;

    @PostMapping
    public ResponseEntity<?> createPlayoff(@RequestBody PlayoffRequestDto playoffRequestDto)
    {
        if(!codeValidator.isValid(playoffRequestDto.getValidationCode()))
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Invalid code."));
        }

        PlayoffResponseDto response = playoffService.createPlayoff(playoffRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<?> getAllPlayoffs(@RequestParam(name = "limit", defaultValue = "4") int limit,
                                            @RequestParam(name = "offset", defaultValue = "0") int offset)
    {
        return ResponseEntity.ok(playoffService.getAllPlayoffs(limit, offset));
    }

    @GetMapping("/{playoffId}")
    public ResponseEntity<?> getPlayoffById(@PathVariable("playoffId") Long playoffId)
    {
        Playoff playoff = playoffRepository.findById(playoffId)
            .orElse(null);

        if(playoff == null)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(playoffService.getPlayoffById(playoffId));
    }
}
