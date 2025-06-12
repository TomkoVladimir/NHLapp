package com.playground.example.learning.service.impl;

import com.playground.example.learning.dto.TeamDto;
import com.playground.example.learning.entity.Team;
import com.playground.example.learning.mapper.TeamMapper;
import com.playground.example.learning.repository.TeamRepository;
import com.playground.example.learning.service.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TeamServiceImpl implements TeamService
{
    private final TeamRepository teamRepository;

    @Override
    public TeamDto createTeam(TeamDto teamDto)
    {
        Team team = TeamMapper.mapToEntity(teamDto);
        Team savedTeam = teamRepository.save(team);
        return TeamMapper.mapToTeamDto(savedTeam);
    }
}
