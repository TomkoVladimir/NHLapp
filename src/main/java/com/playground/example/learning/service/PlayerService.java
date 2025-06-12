package com.playground.example.learning.service;

import com.playground.example.learning.dto.PlayerDto.PlayerRequestDto;
import com.playground.example.learning.dto.PlayerDto.PlayerResponseDto;
import com.playground.example.learning.dto.PlayerDto.PlayerStatsDto;

public interface PlayerService
{
    PlayerResponseDto createPlayer(PlayerRequestDto requestDto);

    PlayerStatsDto getPlayerStats(Long playerId);
}
