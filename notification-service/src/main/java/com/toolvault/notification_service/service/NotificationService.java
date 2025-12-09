package com.toolvault.notification_service.service;

import com.toolvault.notification_service.model.EmailNotificationRequest;
import com.toolvault.notification_service.model.EventNotificationRequest;
import com.toolvault.notification_service.model.SmsNotificationRequest;

public interface NotificationService {
    void sendEmail(EmailNotificationRequest req);
    void sendSms(SmsNotificationRequest req);
    void sendEvent(EventNotificationRequest req);
}
