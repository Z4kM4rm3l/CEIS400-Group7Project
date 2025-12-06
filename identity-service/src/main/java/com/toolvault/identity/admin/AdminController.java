package com.toolvault.identity.admin;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AdminController {

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/admin/ping")
  public Map<String, String> ping() {
    return Map.of("status", "pong");
  }
}
