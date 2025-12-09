package com.toolvault.notification_service.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SmsNotificationRequest {

    @NotBlank
    @Size(min = 10, max = 20)
    private String toNumber;

    @NotBlank
    @Size(max = 160)
    private String message;

    // getters & setters
    public String getToNumber() { return toNumber; }
    public void setToNumber(String toNumber) { this.toNumber = toNumber; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
