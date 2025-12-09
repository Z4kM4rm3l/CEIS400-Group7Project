package com.toolvault.notification_service.controller;

import com.toolvault.notification_service.model.EmailNotificationRequest;
import com.toolvault.notification_service.model.SmsNotificationRequest;
import com.toolvault.notification_service.model.EventNotificationRequest;
import com.toolvault.notification_service.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Notification API", description = "Email, SMS, and generic event notifications")
@RestController
@RequestMapping("/notify") // with context-path '/api', full path = '/api/notify/...'
public class NotificationController {

    private final NotificationService svc;

    public NotificationController(NotificationService svc) {
        this.svc = svc;
    }

    @Operation(summary = "Send email notification")
    @PostMapping("/email")
    public ResponseEntity<Void> email(@Valid @RequestBody EmailNotificationRequest req) {
        svc.sendEmail(req);
        return ResponseEntity.accepted().build(); // 202
    }

    @Operation(summary = "Send SMS notification")
    @PostMapping("/sms")
    public ResponseEntity<Void> sms(@Valid @RequestBody SmsNotificationRequest req) {
        svc.sendSms(req);
        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Publish generic event")
    @PostMapping("/event")
    public ResponseEntity<Void> event(@Valid @RequestBody EventNotificationRequest req) {
        svc.sendEvent(req);
        return ResponseEntity.accepted().build();
    }
}
