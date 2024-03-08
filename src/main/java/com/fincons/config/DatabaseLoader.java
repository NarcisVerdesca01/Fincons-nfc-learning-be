package com.fincons.config;

import com.fincons.entity.Role;
import com.fincons.entity.User;
import com.fincons.repository.RoleRepository;
import com.fincons.repository.UserRepository;
import com.fincons.service.authorization.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabaseLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        populateAdmin();
    }


    private void populateAdmin() {

        if (
                userRepository.findByEmail("admin@gmail.com") == null
                        &&
                roleRepository.findByName("ROLE_ADMIN") == null
        ) {
            User admin = new User();
            admin.setFirstName("admin");
            admin.setLastName("admin");
            admin.setEmail("admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("Password!"));
            Role role = authService.roleToAssign("ROLE_ADMIN");
            admin.setRoles(List.of(role));
            User admnSaved = userRepository.save(admin);
        }
    }

}