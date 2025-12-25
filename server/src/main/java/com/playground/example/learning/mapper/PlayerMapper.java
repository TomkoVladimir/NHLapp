package com.playground.example.learning.mapper;

import com.playground.example.learning.dto.PlayerDto.PlayerRequestDto;
import com.playground.example.learning.dto.PlayerDto.PlayerResponseDto;
import com.playground.example.learning.entity.Player;

public class PlayerMapper
{
    public static Player toPlayerEntity(PlayerRequestDto playerRequestDto)
    {
        Player player = new Player();
        player.setFirstName(playerRequestDto.getFirstName());
        player.setLastName(playerRequestDto.getLastName());
        player.setNickName(playerRequestDto.getNickName());
        return player;
    }

    public static PlayerResponseDto toResponseDto(Player player) {
        return new PlayerResponseDto(player.getNickName());
    }
}
