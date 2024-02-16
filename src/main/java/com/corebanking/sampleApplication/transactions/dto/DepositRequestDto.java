package com.corebanking.sampleApplication.transactions.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepositRequestDto {
    private BigDecimal transactionAmount;
    private String notes;
    private String receiverAccountNumber;
    private String senderName;
}
