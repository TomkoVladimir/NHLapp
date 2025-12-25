package com.playground.example.learning.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidMatchDataException extends RuntimeException
{
    public InvalidMatchDataException(String message)
    {
        super(message);
    }
}
