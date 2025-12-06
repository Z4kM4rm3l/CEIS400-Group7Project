package com.toolvault.identity.bootstrap;

import com.toolvault.identity.user.User;
import com.toolvault.identity.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {
  private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

  private final UserRepository users;
  private final PasswordEncoder encoder;

  public DataSeeder(UserRepository users, PasswordEncoder encoder) {
    this.users = users;
    this.encoder = encoder;
  }

  @Override
  public void run(String... args) {
    seedUser("employee@toolvault.local", "Emp123!", "USER");
    seedUser("manager@toolvault.local", "Mgr123!", "ADMIN");
  }

  private void seedUser(String email, String rawPassword, String role) {
    if (users.existsByEmail(email)) {
      return;
    }
    User u = new User();
    u.setEmail(email);
    u.setPasswordHash(encoder.encode(rawPassword));
    u.setRole(role);
    users.save(u);
    log.info("Seeded user: {} (role={})", email, role);
  }
}
