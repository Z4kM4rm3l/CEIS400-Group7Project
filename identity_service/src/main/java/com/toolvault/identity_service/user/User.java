package com.toolvault.identity_service.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Email @NotBlank @Column(nullable = false)
  private String email;
  @NotBlank @Column(nullable = false, name = "password_hash")
  private String passwordHash;
  @Column(nullable = false)
  private String role = "USER";

  public User() {}
  public Long getId() { return id; } public void setId(Long id) { this.id = id; }
  public String getEmail() { return email; } public void setEmail(String email) { this.email = email; }
  public String getPasswordHash() { return passwordHash; } public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
  public String getRole() { return role; } public void setRole(String role) { this.role = role; }
}
