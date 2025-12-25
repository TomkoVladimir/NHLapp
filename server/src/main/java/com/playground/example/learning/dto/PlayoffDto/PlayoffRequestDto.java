package com.playground.example.learning.dto.PlayoffDto;

import com.playground.example.learning.entity.Player;
import com.playground.example.learning.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PlayoffRequestDto
{
    private String playoffTitle;
    private String playerOneNickName;
    private String playerTwoNickName;
    private String playerThreeNickName;
    private String playerFourNickName;
    private String teamOneName;
    private String teamTwoName;
    private String teamThreeName;
    private String teamFourName;
    private String validationCode;
}
