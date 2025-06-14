package com.playground.example.learning.dto.PlayoffDto;

import com.playground.example.learning.entity.Player;
import com.playground.example.learning.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PlayoffRequestDto
{
    private String playoffTitle;
    List<Player> players;
    List<Team> teams;
}
