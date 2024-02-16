package com.corebanking.sampleApplication.account.service;

import com.corebanking.sampleApplication.account.dto.AccountDetailsResponseDto;
import com.corebanking.sampleApplication.account.dto.BaseResponse;
import com.corebanking.sampleApplication.account.dto.CreateAccountRequestDto;
import com.corebanking.sampleApplication.account.dto.CreateCustomerResponseDto;
import com.corebanking.sampleApplication.account.dto.UpdateAccountRequestDto;

public interface AccountService {
    CreateCustomerResponseDto createCustomerAccount (CreateAccountRequestDto createAccountRequestDto);
    AccountDetailsResponseDto retrieveAccountDetails (String accountNumber);
    BaseResponse updateAccountDetails (String accountNumber, UpdateAccountRequestDto updateAccountRequestDto);
    BaseResponse closeAccount(String accountNumber, String command);

}
