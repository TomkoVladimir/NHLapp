package com.playground.example.learning.service;

import com.playground.example.learning.dto.PlayerDto.PlayerRequestDto;
import com.playground.example.learning.dto.PlayerDto.PlayerResponseDto;

public interface PlayerService
{
    PlayerResponseDto createPlayer(PlayerRequestDto requestDto);

    java.util.List<PlayerResponseDto> getAllPlayers();
}
