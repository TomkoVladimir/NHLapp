package com.playground.example.learning.service;

import com.playground.example.learning.dto.PlayoffDto.PlayoffRequestDto;
import com.playground.example.learning.dto.PlayoffDto.PlayoffResponseDto;

import java.util.List;

public interface PlayoffService
{
    PlayoffResponseDto createPlayoff(PlayoffRequestDto requestDto);

    List<PlayoffResponseDto> getAllPlayoffs(int limit, int offset);

    PlayoffResponseDto getPlayoffById(Long playoffId);
}
