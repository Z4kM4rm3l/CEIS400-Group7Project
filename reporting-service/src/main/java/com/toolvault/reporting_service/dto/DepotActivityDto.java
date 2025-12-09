package com.toolvault.reporting_service.dto;

import java.time.Instant;

public class DepotActivityDto {
    private String equipmentId;
    private String user;
    private String action;     // CHECK_IN / CHECK_OUT
    private Instant timestamp;

    public String getEquipmentId() { return equipmentId; }
    public void setEquipmentId(String equipmentId) { this.equipmentId = equipmentId; }

    public String getUser() { return user; }
    public void setUser(String user) { this.user = user; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}
