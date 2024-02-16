package com.corebanking.sampleApplication.transactions.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DebitRequestDto {
    private String accountNumber;
    private BigDecimal amount;
}
