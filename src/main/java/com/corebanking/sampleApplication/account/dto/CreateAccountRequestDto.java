package com.corebanking.sampleApplication.account.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateAccountRequestDto {
    @NotEmpty(message = "First name cannot be empty")
    private String firstname;

    @NotEmpty(message = "Last name cannot be empty")
    private String lastname;

    @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10}$", message = "Invalid phone number format")
    @NotEmpty(message = "Phone number cannot be empty")
    private String phoneNumber;

    @Pattern(regexp = "^(savings|current|fixed)$", message = "Invalid account type. Valid values are 'Savings', 'Current', or 'Fixed'.")
    @NotEmpty(message = "accountType cannot be empty")
    private String accountType;
}
