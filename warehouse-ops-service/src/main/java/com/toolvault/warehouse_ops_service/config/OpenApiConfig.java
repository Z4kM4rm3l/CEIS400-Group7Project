package com.toolvault.warehouse_ops_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI api() {
        return new OpenAPI().info(new Info()
                .title("ToolVault Warehouse Ops API")
                .version("1.0")
                .description("Inventory stock, allocations, restock, and top SKUs"));
    }
}
