package com.toolvault.identity_service.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
  private final UserRepository repository; private final PasswordEncoder passwordEncoder;
  public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
    this.repository = repository; this.passwordEncoder = passwordEncoder;
  }
  public User register(String email, String rawPassword) {
    if (repository.existsByEmail(email)) { throw new IllegalStateException("email_in_use"); }
    User u = new User(); u.setEmail(email); u.setPasswordHash(passwordEncoder.encode(rawPassword)); u.setRole("USER");
    return repository.save(u);
  }
  public Optional<User> findByEmail(String email) { return repository.findByEmail(email); }
}
