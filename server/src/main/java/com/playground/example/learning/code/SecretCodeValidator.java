package com.playground.example.learning.code;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SecretCodeValidator {

    @Value("${app.validation-code}")
    private String expectedCode;

    public boolean isValid(String inputCode) {
        return expectedCode.equals(inputCode);
    }
}