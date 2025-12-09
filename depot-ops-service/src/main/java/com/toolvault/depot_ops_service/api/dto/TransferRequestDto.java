package com.toolvault.depot_ops_service.api.dto;

import jakarta.validation.constraints.NotBlank;

public record TransferRequestDto(
        @NotBlank String assetTag,
        @NotBlank String destWarehouseCode
) {}
