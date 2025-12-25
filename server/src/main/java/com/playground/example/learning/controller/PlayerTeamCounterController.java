package com.playground.example.learning.controller;

import com.playground.example.learning.dto.FavouriteTeamDto;
import com.playground.example.learning.entity.Player;
import com.playground.example.learning.entity.Team;
import com.playground.example.learning.repository.PlayerTeamCounterRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/player-team-counters")
public class PlayerTeamCounterController {

    private final PlayerTeamCounterRepository playerTeamCounterRepository;

    public PlayerTeamCounterController(PlayerTeamCounterRepository playerTeamCounterRepository) {
        this.playerTeamCounterRepository = playerTeamCounterRepository;
    }

    @GetMapping("/favouriteTeams")
    public List<FavouriteTeamDto> getFavoriteTeams() {
        List<Object[]> results = playerTeamCounterRepository.findFavoriteTeamForEachPlayer();

        // Map Object[] to DTO for better JSON response
        return results.stream().map(row -> {
            Player player = (Player) row[0];
            Team team = (Team) row[1];
            Integer matchCount = (Integer) row[2];
            return new FavouriteTeamDto(player.getId(), player.getNickName(), team, matchCount);
        }).toList();
    }
}
