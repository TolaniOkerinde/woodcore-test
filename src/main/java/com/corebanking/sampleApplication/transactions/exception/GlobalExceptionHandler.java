package com.corebanking.sampleApplication.transactions.exception;

import com.corebanking.sampleApplication.account.exceptions.AccountNumberNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class GlobalExceptionHandler {

    @ExceptionHandler(AccountNumberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleAccountNumberNotFoundException(AccountNumberNotFoundException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(FailedTransactionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleFailedTransactionException(FailedTransactionException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleInsufficientBalanceException(InsufficientBalanceException ex) {
        return new ErrorResponse(ex.getMessage());
    }


    public static class ErrorResponse {
        private final String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}