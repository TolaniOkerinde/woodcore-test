package com.corebanking.sampleApplication.transactions.controller;

import com.corebanking.sampleApplication.transactions.dto.DepositRequestDto;
import com.corebanking.sampleApplication.transactions.dto.WithdrawMoneyRequestDto;
import com.corebanking.sampleApplication.transactions.dto.DepositResponseDto;
import com.corebanking.sampleApplication.transactions.dto.TransferMoneyRequestDto;
import com.corebanking.sampleApplication.transactions.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    @PostMapping("/withdraw")
    public ResponseEntity<DepositResponseDto> withdrawMoney(
            @Valid @RequestBody WithdrawMoneyRequestDto request) {
        return ResponseEntity.ok(transactionService.withdrawMoney(request));
    }

    @PostMapping("/transfer")
    public ResponseEntity<DepositResponseDto> transferMoney(
            @Valid @RequestBody TransferMoneyRequestDto request) {
        return ResponseEntity.ok(transactionService.transferMoney(request));
    }

    @PostMapping("/deposit")
    public ResponseEntity<DepositResponseDto> depositMoney(
            @Valid @RequestBody DepositRequestDto request) {
        return ResponseEntity.ok(transactionService.depositMoney(request));
    }

}
