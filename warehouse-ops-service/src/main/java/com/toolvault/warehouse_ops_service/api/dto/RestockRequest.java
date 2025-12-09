package com.toolvault.warehouse_ops_service.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record RestockRequest(@NotBlank String sku, @Min(1) int quantity, String name) {}
