package com.playground.example.learning.service;

import com.playground.example.learning.dto.SeriesDto.SeriesRequestDto;
import com.playground.example.learning.dto.SeriesDto.SeriesResponseDto;
import com.playground.example.learning.entity.Match;
import com.playground.example.learning.entity.Series;

import java.util.List;

public interface SeriesService
{
    SeriesResponseDto createSeries(SeriesRequestDto dto);

    void updateSeriesAfterMatch(Match match);

    Match createNextSeriesMatch(Series series, int matchNumber);

    List<SeriesResponseDto> getAllSeries(int limit, int offset);
}
