package com.playground.example.learning.mapper;

import com.playground.example.learning.dto.SeriesDto.SeriesResponseDto;
import com.playground.example.learning.entity.Match;
import com.playground.example.learning.entity.Series;

public class SeriesMapper
{
    public static SeriesResponseDto toResponseDto(Series series) {
        SeriesResponseDto dto = new SeriesResponseDto();
        dto.setSeriesId(series.getId());
        dto.setCreatedAt(series.getCreatedAt());
        dto.setPlayerOneNickName(series.getPlayerOne().getNickName()); // assuming Player has getNickname()
        dto.setPlayerTwoNickName(series.getPlayerTwo().getNickName());
        dto.setTeamOneName(series.getTeamOne());
        dto.setTeamTwoName(series.getTeamTwo());
        dto.setWinner(series.getWinner() != null ? series.getWinner().getNickName() : null);
        dto.setMatchIds(series.getMatches().stream()
                .map(Match::getId)
                .toList());
        dto.setCompleted(series.isCompleted());
        dto.setPlayoffId(series.getPlayoff() != null ? series.getPlayoff().getId() : null);// Assuming Series has a getMatches() method that returns a
        return dto;
    }
}
