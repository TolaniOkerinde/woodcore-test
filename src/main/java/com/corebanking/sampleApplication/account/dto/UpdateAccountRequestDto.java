package com.corebanking.sampleApplication.account.dto;

import lombok.Data;

@Data
public class UpdateAccountRequestDto {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String accountType;
    private String status;
}
