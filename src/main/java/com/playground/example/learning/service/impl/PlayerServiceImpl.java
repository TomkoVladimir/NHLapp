package com.playground.example.learning.service.impl;

import com.playground.example.learning.dto.PlayerDto.PlayerRequestDto;
import com.playground.example.learning.dto.PlayerDto.PlayerResponseDto;
import com.playground.example.learning.dto.PlayerDto.PlayerStatsDto;
import com.playground.example.learning.entity.Player;
import com.playground.example.learning.exception.ResourceNotFoundException;
import com.playground.example.learning.mapper.PlayerMapper;
import com.playground.example.learning.repository.PlayerRepository;
import com.playground.example.learning.service.PlayerService;
import com.playground.example.learning.service.PlayerStatsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PlayerServiceImpl implements PlayerService
{
    private final PlayerRepository playerRepository;
    private final PlayerStatsService playerStatsService;

    @Override
    public PlayerResponseDto createPlayer(PlayerRequestDto requestDto)
    {
        Player player = PlayerMapper.toPlayerEntity(requestDto);
        Player savedPlayer = playerRepository.save(player);
        return PlayerMapper.toResponseDto(savedPlayer);
    }

    @Override
    public PlayerStatsDto getPlayerStats(Long playerId)
    {
        Player player = playerRepository.findById(playerId)
            .orElseThrow(() -> new ResourceNotFoundException("Player not found"));

        return new PlayerStatsDto(
            player.getId(),
            player.getFirstName(),
            playerStatsService.getFavoriteTeam(player),
            0,
            0,
            0,
            0,
            0,
            "N/A",
            "N/A",
            0,
            0,
            0,
            0.0,
            0,
            0
        );
    }

    public List<PlayerResponseDto> getAllPlayers() {
        return playerRepository.findAll().stream()
            .map(PlayerMapper::toResponseDto)
            .collect(Collectors.toList());
    }
}
