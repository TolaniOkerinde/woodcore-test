package com.corebanking.sampleApplication.account.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
@Getter
@Setter
@ConfigurationProperties
@Component
public class Properties {
    private String activeStatus = "Active";
    private String closedStatus = "closed";
    private String debit = "DEBIT";
    private String credit = "CREDIT";

}
