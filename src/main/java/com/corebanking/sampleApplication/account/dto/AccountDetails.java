package com.corebanking.sampleApplication.account.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class AccountDetails {
    private String accountNumber;
    private String accountType;
    private String status;
    private BigDecimal accountBalance;
    private Instant closedOnDate;
    private String displayName;
}
