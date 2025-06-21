package com.playground.example.learning.controller;

import com.playground.example.learning.dto.TeamDto;
import com.playground.example.learning.service.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
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

    @GetMapping
    public ResponseEntity<List<TeamDto>> getAllTeams() {
        List<TeamDto> teams = teamService.getAllTeams();
        return ResponseEntity.ok(teams);
    }

}
