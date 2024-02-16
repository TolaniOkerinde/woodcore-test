package com.corebanking.sampleApplication.account.controller;

import com.corebanking.sampleApplication.account.dto.AccountDetailsResponseDto;
import com.corebanking.sampleApplication.account.dto.BaseResponse;
import com.corebanking.sampleApplication.account.dto.CreateAccountRequestDto;
import com.corebanking.sampleApplication.account.dto.CreateCustomerResponseDto;
import com.corebanking.sampleApplication.account.dto.UpdateAccountRequestDto;
import com.corebanking.sampleApplication.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final AccountService accountService;

    @PostMapping()
    public ResponseEntity<CreateCustomerResponseDto> createCustomer(@Valid @RequestBody CreateAccountRequestDto request) {
        return ResponseEntity.ok(accountService.createCustomerAccount(request));
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountDetailsResponseDto> retrieveCustomer(@PathVariable String accountNumber) {
        return ResponseEntity.ok(accountService.retrieveAccountDetails(accountNumber));
    }

    @PutMapping("/{accountNumber}")
    public ResponseEntity<BaseResponse> updateCustomer(@Valid @RequestBody UpdateAccountRequestDto request, @PathVariable String accountNumber) {
        return ResponseEntity.ok(accountService.updateAccountDetails(accountNumber, request));
    }

    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<BaseResponse> closeCustomerAccount(@PathVariable String accountNumber, @RequestParam String command) {
        return ResponseEntity.ok(accountService.closeAccount(accountNumber, command));
    }
}
