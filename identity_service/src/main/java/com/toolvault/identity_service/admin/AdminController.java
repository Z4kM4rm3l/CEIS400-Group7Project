// src/main/java/com/toolvault/identity_service/admin/AdminController.java
package com.toolvault.identity_service.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
    @GetMapping("/admin/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("ok");
    }
}
