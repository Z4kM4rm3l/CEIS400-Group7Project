// src/test/java/com/toolvault/identity_service/TestBootConfig.java
package com.toolvault.identity_service;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.toolvault.identity_service")
public class TestBootConfig {
    // No beans here; let SecurityConfig and auto-config handle everything
}
