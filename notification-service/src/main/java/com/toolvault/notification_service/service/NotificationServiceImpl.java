package com.toolvault.notification_service.service;

import com.toolvault.notification_service.model.EmailNotificationRequest;
import com.toolvault.notification_service.model.EventNotificationRequest;
import com.toolvault.notification_service.model.SmsNotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Override
    public void sendEmail(EmailNotificationRequest req) {
        // In demo, just log. (If Mailhog is configured, you can wire JavaMailSender later.)
        log.info("Email queued: to={} subject='{}'", req.getTo(), req.getSubject());
    }

    @Override
    public void sendSms(SmsNotificationRequest req) {
        log.info("SMS queued: to={} message='{}'", req.getToNumber(), req.getMessage());
    }

    @Override
    public void sendEvent(EventNotificationRequest req) {
        log.info("Event received: type={} entityId={} metadata={}", req.getEventType(), req.getEntityId(), req.getMetadata());
    }
}
