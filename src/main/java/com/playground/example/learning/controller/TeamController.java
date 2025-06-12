package com.playground.example.learning.controller;

import com.playground.example.learning.dto.TeamDto;
import com.playground.example.learning.service.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teams")
@AllArgsConstructor
public class TeamController
{
    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<TeamDto> createTeam(@RequestBody TeamDto teamDto) {
        TeamDto createdTeam = teamService.createTeam(teamDto);
        return new ResponseEntity<>(createdTeam, HttpStatus.CREATED);
    }
}
