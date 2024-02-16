package com.corebanking.sampleApplication.account.dto;

public class BaseResponse {
    private String status;
    private String defaultMessage;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public void setDefaultMessage(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "status='" + status + '\'' +
                ", defaultMessage='" + defaultMessage + '\'' +
                '}';
    }
}
