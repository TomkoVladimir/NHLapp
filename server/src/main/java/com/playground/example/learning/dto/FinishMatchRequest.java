package com.playground.example.learning.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FinishMatchRequest
{
    private int htScore;
    private int atScore;
    private boolean overTime;
    private String validationCode;
}
