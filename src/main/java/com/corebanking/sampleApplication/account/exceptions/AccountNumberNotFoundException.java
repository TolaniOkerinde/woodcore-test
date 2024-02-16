package com.corebanking.sampleApplication.account.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AccountNumberNotFoundException extends RuntimeException {
    public AccountNumberNotFoundException(String message) {
        super(message);
    }
}


