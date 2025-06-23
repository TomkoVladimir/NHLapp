package com.playground.example.learning.dto.SeriesDto;

import com.playground.example.learning.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeriesResponseDto {
    private Long seriesId;
    private LocalDateTime createdAt;
    private String playerOneNickName;
    private String playerTwoNickName;
    private Team teamOneName;
    private Team teamTwoName;
    private String winner;
    private Long playoffId; // Optional, can be null
    private List<Long> matchIds; // Matches created at series creation
    private boolean completed;
}