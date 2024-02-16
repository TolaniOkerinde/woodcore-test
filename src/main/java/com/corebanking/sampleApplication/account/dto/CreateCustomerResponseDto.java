package com.corebanking.sampleApplication.account.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class CreateCustomerResponseDto extends  BaseResponse {
    private String accountNumber;
    private Long customerId;

}
