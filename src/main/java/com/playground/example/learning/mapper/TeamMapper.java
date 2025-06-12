package com.playground.example.learning.mapper;

import com.playground.example.learning.dto.TeamDto;
import com.playground.example.learning.entity.Team;

public class TeamMapper
{
    public static TeamDto mapToTeamDto(Team team)
    {
        return new TeamDto(
            team.getId(),
            team.getName()
        );
    }

    public static Team mapToEntity(TeamDto teamDto)
    {
        return new Team(
            teamDto.getId(),
            teamDto.getName()
        );
    }
}
