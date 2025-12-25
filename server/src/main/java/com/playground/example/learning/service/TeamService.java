package com.playground.example.learning.service;

import com.playground.example.learning.dto.TeamDto;

import java.util.List;

public interface TeamService
{
    TeamDto createTeam(TeamDto teamDto);

    List<TeamDto> getAllTeams();
}
