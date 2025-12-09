package com.toolvault.procurement_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ProcurementStatus", description = "Status of a purchase request.")
public class ProcurementStatusDto {

    @Schema(description = "Unique ID of the purchase request", example = "42")
    private Long id;

    @Schema(description = "Stock keeping unit identifier", example = "GB-ITEM-001")
    private String sku;

    @Schema(description = "Requested quantity", example = "5")
    private int quantity;

    @Schema(description = "Current status of the request (e.g., PENDING, APPROVED, REJECTED)", example = "PENDING")
    private String status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
