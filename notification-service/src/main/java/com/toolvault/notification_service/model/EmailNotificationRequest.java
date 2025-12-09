package com.toolvault.notification_service.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EmailNotificationRequest {

    @Email
    @NotBlank
    private String to;

    @NotBlank
    @Size(max = 160)
    private String subject;

    @NotBlank
    private String body;

    // getters & setters
    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
}
