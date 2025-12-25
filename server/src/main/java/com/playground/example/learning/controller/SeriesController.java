package com.playground.example.learning.controller;

import com.playground.example.learning.code.SecretCodeValidator;
import com.playground.example.learning.dto.SeriesDto.SeriesRequestDto;
import com.playground.example.learning.dto.SeriesDto.SeriesResponseDto;
import com.playground.example.learning.entity.Series;
import com.playground.example.learning.mapper.SeriesMapper;
import com.playground.example.learning.repository.SeriesRepository;
import com.playground.example.learning.service.SeriesService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/series")
@AllArgsConstructor
public class SeriesController
{
    private final SeriesService seriesService;
    SeriesRepository seriesRepository;
    private final SecretCodeValidator codeValidator;

    @PostMapping
    public ResponseEntity<?> createSeries(@RequestBody SeriesRequestDto seriesRequestDto)
    {
        if (!codeValidator.isValid(seriesRequestDto.getValidationCode())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Invalid code."));
        }

        SeriesResponseDto response = seriesService.createSeries(seriesRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<SeriesResponseDto>> getAllSeries(@RequestParam(name= "limit", defaultValue = "6") int limit,
                                                                @RequestParam(name= "offset", defaultValue = "0") int offset)
    {
        List<SeriesResponseDto> seriesList = seriesService.getAllSeries(limit, offset);
        return ResponseEntity.ok(seriesList);
    }

    @GetMapping("/{seriesId}")
    public ResponseEntity<SeriesResponseDto> getSeriesById(@PathVariable("seriesId") Long seriesId)
    {
        Series series = seriesRepository.findById(seriesId)
            .orElse(null);

        if (series == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        SeriesResponseDto dto = SeriesMapper.toResponseDto(series);
        return ResponseEntity.ok(dto);
    }
}
