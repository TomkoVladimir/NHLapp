package com.playground.example.learning.service.impl;

import com.playground.example.learning.entity.Player;
import com.playground.example.learning.entity.PlayerTeamCounter;
import com.playground.example.learning.repository.PlayerTeamCounterRepository;
import com.playground.example.learning.service.PlayerStatsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class PlayerStatsServiceImpl implements PlayerStatsService
{
    private final PlayerTeamCounterRepository playerTeamCounterRepository;

    @Override
    public String getFavoriteTeam(Player player)
    {
        List<PlayerTeamCounter> counters = playerTeamCounterRepository.findAllByPlayer(player);

        return counters.stream()
            .max(Comparator.comparingInt(PlayerTeamCounter::getMatchCount))
            .map(counter -> counter.getTeam().getName()) // Or any display property
            .orElse("N/A");
    }
}
