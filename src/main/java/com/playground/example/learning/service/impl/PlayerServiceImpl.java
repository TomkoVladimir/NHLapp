package com.playground.example.learning.service.impl;

import com.playground.example.learning.dto.PlayerDto.PlayerRequestDto;
import com.playground.example.learning.dto.PlayerDto.PlayerResponseDto;
import com.playground.example.learning.entity.Player;
import com.playground.example.learning.exception.PlayerAlreadyExistsException;
import com.playground.example.learning.mapper.PlayerMapper;
import com.playground.example.learning.repository.PlayerRepository;
import com.playground.example.learning.service.PlayerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PlayerServiceImpl implements PlayerService
{
    private final PlayerRepository playerRepository;

    @Override
    public PlayerResponseDto createPlayer(PlayerRequestDto requestDto)
    {
        Optional<Player> existing = playerRepository.findByNickName(requestDto.getNickName());
        if (existing.isPresent()) {
            throw new PlayerAlreadyExistsException("Player with nickname '" + requestDto.getNickName() + "' already exists");
        }

        Player player = PlayerMapper.toPlayerEntity(requestDto);
        Player savedPlayer = playerRepository.save(player);
        return PlayerMapper.toResponseDto(savedPlayer);
    }

    public List<PlayerResponseDto> getAllPlayers()
    {
        return playerRepository.findAll().stream()
            .map(PlayerMapper::toResponseDto)
            .collect(Collectors.toList());
    }
}
