package com.corebanking.sampleApplication.transactions.dto;

import com.corebanking.sampleApplication.account.dto.BaseResponse;
import lombok.Data;

@Data
public class DepositResponseDto extends BaseResponse {
    private Long transactionId;

}
