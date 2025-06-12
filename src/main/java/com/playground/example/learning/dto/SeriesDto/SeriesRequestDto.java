package com.playground.example.learning.dto.SeriesDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeriesRequestDto {
    private String playerOneNickName;
    private String playerTwoNickName;
    private String teamOneName;
    private String teamTwoName;
}