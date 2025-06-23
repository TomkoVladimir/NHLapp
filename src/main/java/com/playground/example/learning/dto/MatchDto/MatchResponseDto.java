package com.playground.example.learning.dto.MatchDto;

import com.playground.example.learning.entity.Team;
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
    private Long id;
    private String matchDate;
    private String htPlayerNickName;
    private String atPlayerNickName;
    private Team homeTeam;
    private Team awayTeam;
    private Integer htScore;
    private Integer atScore;
    private Boolean overTime;
    private Integer htPoints;
    private Integer atPoints;
}
