package com.tpaWorkers.SurveyService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class SurveyException extends Exception {
    public SurveyException(String message) {
        super(message);
    }
}
