package com.corebanking.sampleApplication.transactions.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AccountNotInActiveStateException extends RuntimeException {

    public AccountNotInActiveStateException(String message) {
        super(message);
    }
}