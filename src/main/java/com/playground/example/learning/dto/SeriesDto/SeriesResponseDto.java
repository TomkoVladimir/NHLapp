package com.playground.example.learning.dto.SeriesDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeriesResponseDto {
    private Long seriesId;
    private String playerOneNickName;
    private String playerTwoNickName;
    private String teamOneName;
    private String teamTwoName;
    private String winner;
    private Long playoffId; // Optional, can be null
    private List<Long> matchIds; // Matches created at series creation
    private boolean completed;
}