package com.toolvault.depot_ops_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.toolvault.depot_ops_service")
@EnableJpaRepositories(basePackages = "com.toolvault.depot_ops_service.repository")
@EntityScan(basePackages = "com.toolvault.depot_ops_service.domain")
public class DepotOpsApplication {
    public static void main(String[] args) {
        SpringApplication.run(DepotOpsApplication.class, args);
    }
}
