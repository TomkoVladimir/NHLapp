package com.playground.example.learning.service;

import com.playground.example.learning.dto.MatchDto.MatchRequestDto;
import com.playground.example.learning.dto.MatchDto.MatchResponseDto;

import java.util.List;

public interface MatchesService
{
    MatchResponseDto createStandaloneMatch(MatchRequestDto dto);

    MatchResponseDto finishMatch(Long matchId, int htScore, int atScore, boolean overTime);

    List<MatchResponseDto> getAllMatches(int limit, int offset);

    MatchResponseDto getMatchById(Long matchId);

    void deleteMatchById(Long matchId);
}
