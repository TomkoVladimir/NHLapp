package com.playground.example.learning.dto.MatchDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MatchResponseDto
{
    private String matchDate;
    private String htPlayerNickName;
    private String atPlayerNickName;
    private String homeTeam;
    private String awayTeam;
    private Integer htScore;
    private Integer atScore;
    private Boolean overTime;
    private Integer htPoints;
    private Integer atPoints;
}
