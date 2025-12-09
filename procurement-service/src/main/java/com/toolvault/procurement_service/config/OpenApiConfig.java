package com.toolvault.procurement_service.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Procurement Service API",
                version = "v1",
                description = "Endpoints for purchase requests and status updates (approve/reject)."
        )
)
@Configuration
public class OpenApiConfig { /* no beans required for basic UI */ }
