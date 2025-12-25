package com.playground.example.learning.dto;

import com.playground.example.learning.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FavouriteTeamDto
{
    private Long playerId;
    private String nickName;
    private Team team;
    private int matchCount;
}
