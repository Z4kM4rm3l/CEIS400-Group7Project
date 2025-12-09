package com.toolvault.depot_ops_service.api.dto;

import jakarta.validation.constraints.NotBlank;

public record CheckInRequest(@NotBlank String tag, String model) {}
