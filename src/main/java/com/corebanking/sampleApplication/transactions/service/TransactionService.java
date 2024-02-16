package com.corebanking.sampleApplication.transactions.service;

import com.corebanking.sampleApplication.transactions.dto.DepositRequestDto;
import com.corebanking.sampleApplication.transactions.dto.WithdrawMoneyRequestDto;
import com.corebanking.sampleApplication.transactions.dto.DepositResponseDto;
import com.corebanking.sampleApplication.transactions.dto.TransferMoneyRequestDto;

public interface TransactionService {
    DepositResponseDto depositMoney(DepositRequestDto depositMoneyRequestDto);

    DepositResponseDto withdrawMoney(WithdrawMoneyRequestDto depositMoneyRequestDto);

    DepositResponseDto transferMoney(TransferMoneyRequestDto transferMoneyRequestDto);
}
