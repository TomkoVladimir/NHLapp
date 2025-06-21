package com.playground.example.learning.service;

import com.playground.example.learning.dto.PlayerEfficiencyDto;
import com.playground.example.learning.entity.Player;

import java.util.List;

public interface PlayerStatsService
{
    String getFavoriteTeam(Player player);

    List<PlayerEfficiencyDto> getAllEfficiencies();
}
