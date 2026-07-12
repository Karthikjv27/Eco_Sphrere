package com.EcoSphere.odoo.auth.config;

import com.EcoSphere.odoo.auth.entity.User;
import com.EcoSphere.odoo.auth.entity.UserRole;
import com.EcoSphere.odoo.auth.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserDataInitializer {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserDataInitializer(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public CommandLineRunner seedUsers() {
        return args -> {
            if (userService.findByUsername("admin").isEmpty()) {
                userService.save(User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin123"))
                        .email("admin@ecosphere.com")
                        .role(UserRole.ADMIN)
                        .enabled(true)
                        .build());
            }
            if (userService.findByUsername("manager").isEmpty()) {
                userService.save(User.builder()
                        .username("manager")
                        .password(passwordEncoder.encode("manager123"))
                        .email("manager@ecosphere.com")
                        .role(UserRole.MANAGER)
                        .enabled(true)
                        .build());
            }
            if (userService.findByUsername("employee").isEmpty()) {
                userService.save(User.builder()
                        .username("employee")
                        .password(passwordEncoder.encode("employee123"))
                        .email("employee@ecosphere.com")
                        .role(UserRole.EMPLOYEE)
                        .enabled(true)
                        .build());
            }
        };
    }
}
