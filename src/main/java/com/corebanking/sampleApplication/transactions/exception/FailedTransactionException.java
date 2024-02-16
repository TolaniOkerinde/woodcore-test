package com.corebanking.sampleApplication.transactions.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FailedTransactionException extends RuntimeException {

    public FailedTransactionException(String message) {
        super(message);
    }
}