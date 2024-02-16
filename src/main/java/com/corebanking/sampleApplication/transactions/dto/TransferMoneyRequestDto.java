package com.corebanking.sampleApplication.transactions.dto;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class TransferMoneyRequestDto {
    private BigDecimal transactionAmount;
    private String notes;
    private String senderAccountNumber;
    private String receiverAccountNumber;
}
