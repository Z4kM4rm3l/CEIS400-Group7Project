package com.toolvault.reporting_service.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType; // <-- correct package
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "Reporting Service API", version = "v1",
                description = "Reports for warehouse and depot with PDF/Excel export"))
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,   // <-- enum constant
        scheme = "bearer",
        bearerFormat = "JWT"
)
@Configuration
public class OpenApiConfig { }
