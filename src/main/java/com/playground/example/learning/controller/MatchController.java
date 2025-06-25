package com.playground.example.learning.controller;

import com.playground.example.learning.code.SecretCodeValidator;
import com.playground.example.learning.dto.FinishMatchRequest;
import com.playground.example.learning.dto.MatchDto.MatchRequestDto;
import com.playground.example.learning.dto.MatchDto.MatchResponseDto;
import com.playground.example.learning.service.MatchesService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/matches")
@AllArgsConstructor
public class MatchController
{
    private final MatchesService matchesService;
    private final SecretCodeValidator codeValidator;

    @PostMapping
    public ResponseEntity<?> createMatch(@RequestBody MatchRequestDto matchRequestDto)
    {
        if (!codeValidator.isValid(matchRequestDto.getValidationCode())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Invalid code."));
        }

        MatchResponseDto response = matchesService.createStandaloneMatch(matchRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{matchId}/finish")
    public ResponseEntity<?> finishMatch(
        @PathVariable("matchId") Long matchId,
        @RequestBody FinishMatchRequest request
    )
    {
        if (!codeValidator.isValid(request.getValidationCode())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Invalid code."));
        }

        MatchResponseDto response = matchesService.finishMatch(matchId, request.getHtScore(), request.getAtScore(), request.isOverTime());

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<MatchResponseDto>> getAllMatches(@RequestParam(name= "limit", defaultValue = "30") int limit,
                                                                 @RequestParam(name= "offset", defaultValue = "0") int offset)
    {
        List<MatchResponseDto> matches = matchesService.getAllMatches(limit, offset);
        return ResponseEntity.ok(matches);
    }

    @GetMapping("/{matchId}")
    public ResponseEntity<MatchResponseDto> getMatchById(@PathVariable("matchId") Long matchId)
    {
        MatchResponseDto match = matchesService.getMatchById(matchId);
        return ResponseEntity.ok(match);
    }

    @DeleteMapping("/{matchId}")
    public ResponseEntity<?> deleteMatch(@PathVariable("matchId") Long matchId,
                                         @RequestBody FinishMatchRequest request)
    {
        if (!codeValidator.isValid(request.getValidationCode())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Invalid code."));
        }

        matchesService.deleteMatchById(matchId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
