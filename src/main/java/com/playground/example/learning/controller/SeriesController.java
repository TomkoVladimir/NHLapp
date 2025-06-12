package com.playground.example.learning.controller;

import com.playground.example.learning.dto.SeriesDto.SeriesRequestDto;
import com.playground.example.learning.dto.SeriesDto.SeriesResponseDto;
import com.playground.example.learning.service.SeriesService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/series")
@AllArgsConstructor
public class SeriesController
{
    private final SeriesService seriesService;

    @PostMapping
    public ResponseEntity<SeriesResponseDto> createSeries(@RequestBody SeriesRequestDto seriesRequestDto)
    {
        SeriesResponseDto response = seriesService.createSeries(seriesRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<SeriesResponseDto>> getAllSeries()
    {
        List<SeriesResponseDto> seriesList = seriesService.getAllSeries();
        return ResponseEntity.ok(seriesList);
    }
}
