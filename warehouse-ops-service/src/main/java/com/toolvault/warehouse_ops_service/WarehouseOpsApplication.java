package com.toolvault.warehouse_ops_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.toolvault.warehouse_ops_service")
@EnableJpaRepositories(basePackages = "com.toolvault.warehouse_ops_service.repository")
@EntityScan(basePackages = "com.toolvault.warehouse_ops_service.domain")
public class WarehouseOpsApplication {
    public static void main(String[] args) {
        SpringApplication.run(WarehouseOpsApplication.class, args);
    }
}
