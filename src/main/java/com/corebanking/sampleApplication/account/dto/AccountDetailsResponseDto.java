package com.corebanking.sampleApplication.account.dto;

import lombok.Data;

@Data
public class AccountDetailsResponseDto extends  BaseResponse{
    private CustomerDetails customer;
    private AccountDetails account ;
}
