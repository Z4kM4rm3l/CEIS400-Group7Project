package com.toolvault.procurement_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ProcurementRequest", description = "Request to create a purchase order for a SKU.")
public class ProcurementRequestDto {

    @Schema(description = "Stock keeping unit identifier", example = "GB-ITEM-001", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "sku must not be blank")
    private String sku;

    @Schema(description = "Requested quantity", example = "5", minimum = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @Min(value = 1, message = "quantity must be at least 1")
    private int quantity;

    @Schema(description = "Reason or context for the procurement", example = "Auto-reorder due to threshold breach")
    private String reason;

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
