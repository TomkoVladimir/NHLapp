package com.playground.example.learning.dto.PlayoffDto;

import com.playground.example.learning.dto.SeriesDto.SeriesResponseDto;
import com.playground.example.learning.entity.Series;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PlayoffResponseDto
{
    private Long playoffId;
    private String playoffTitle;
    List<SeriesResponseDto> series;
}
