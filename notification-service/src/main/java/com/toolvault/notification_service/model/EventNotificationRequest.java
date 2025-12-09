package com.toolvault.notification_service.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Map;

public class EventNotificationRequest {

    @NotBlank
    @Size(max = 64)
    private String eventType; // e.g., LOW_STOCK, ASSET_CHECKED_OUT

    @Size(max = 64)
    private String entityId;  // assetId, sku, userId, etc.

    private Map<String, Object> metadata;

    // getters & setters
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getEntityId() { return entityId; }
    public void setEntityId(String entityId) { this.entityId = entityId; }

    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
}
