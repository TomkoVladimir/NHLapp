package com.playground.example.learning.service;

import com.playground.example.learning.dto.PlayoffDto.PlayoffRequestDto;
import com.playground.example.learning.dto.PlayoffDto.PlayoffResponseDto;

public interface PlayoffService
{
    PlayoffResponseDto createPlayoff(PlayoffRequestDto requestDto);
}
