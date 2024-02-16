package com.corebanking.sampleApplication.account.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class CustomerDetails {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Instant createdDate;
}
