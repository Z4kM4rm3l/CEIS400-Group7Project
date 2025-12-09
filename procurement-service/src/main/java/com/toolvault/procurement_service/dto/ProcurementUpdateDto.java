package com.toolvault.procurement_service.dto;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ProcurementUpdate", description = "Update action for a purchase request status.")
public class ProcurementUpdateDto {

    @Schema(description = "Action to perform: APPROVE or REJECT", example = "APPROVE",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "action must not be blank")
    private String action; // APPROVE | REJECT

    @Schema(description = "Optional note or justification", example = "Manager approval")
    private String note;

    @Schema(description = "Actor performing the action (username or id)", example = "manager1")
    private String actor;

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public String getActor() { return actor; }
    public void setActor(String actor) { this.actor = actor; }
}
