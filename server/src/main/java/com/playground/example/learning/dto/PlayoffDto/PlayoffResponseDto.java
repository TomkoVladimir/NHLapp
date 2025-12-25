package com.playground.example.learning.dto.PlayoffDto;

import com.playground.example.learning.dto.SeriesDto.SeriesResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PlayoffResponseDto
{
    private Long playoffId;
    private LocalDateTime createdAt;
    private String playoffTitle;
    List<SeriesResponseDto> series;
    private boolean semiFinalsCompleted;
    private boolean finalsCompleted;
    private String winner;
    private String secondPlace;
    private String thirdPlace;
}
