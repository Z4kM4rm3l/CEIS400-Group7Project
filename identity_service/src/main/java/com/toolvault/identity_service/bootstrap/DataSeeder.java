
package com.toolvault.identity_service.bootstrap;

import com.toolvault.identity_service.user.User;
import com.toolvault.identity_service.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("!test") // <-- do not load this bean when the 'test' profile is active
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
        seedUser("employee@toolvault.local", "Employee123!", "USER");
        seedUser("manager@toolvault.local", "Manager123!", "ADMIN");
    }

    private void seedUser(String email, String rawPassword, String role) {
        if (users.existsByEmail(email)) return;
        User u = new User();
        u.setEmail(email);
        u.setPasswordHash(encoder.encode(rawPassword));
        u.setRole(role);
        users.save(u);
        log.info("Seeded user: {} (role={})", email, role);
    }
}
