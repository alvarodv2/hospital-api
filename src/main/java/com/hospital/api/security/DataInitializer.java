package com.hospital.api.security;

import com.hospital.api.entity.User;
import com.hospital.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.username:}")
    private String adminUsername;

    @Value("${admin.password:}")
    private String adminPassword;

    @Bean
    public CommandLineRunner initAdmin() {
        return args -> {
            boolean adminExists = userRepository.findByRole("ADMIN").isPresent();

            if (!adminExists) {
                if (adminUsername.isBlank() || adminPassword.isBlank()) {
                    System.err.println("⚠️ ADMIN user not created: ADMIN_USERNAME or ADMIN_PASSWORD missing");
                    return;
                }

                User admin = new User();
                admin.setUsername(adminUsername);

                admin.setPassword(passwordEncoder.encode(adminPassword));

                admin.setRole("ADMIN");

                userRepository.save(admin);

                System.out.println("✅ Initial ADMIN user created: " + adminUsername);
            } else {
                System.out.println("ℹ️ ADMIN user already exists");
            }
        };
    }



}