package net.recagency.config;

import net.recagency.dto.UserRegistrationDto;
import net.recagency.model.Role;
import net.recagency.model.User;
import net.recagency.service.UserService;
import net.recagency.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;
    
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        // Create roles if they don't exist
        Role userRole = roleRepository.findByName("ROLE_USER");
        if (userRole == null) {
            userRole = new Role("ROLE_USER");
            roleRepository.save(userRole);
        }
        
        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        if (adminRole == null) {
            adminRole = new Role("ROLE_ADMIN");
            roleRepository.save(adminRole);
        }

        // Create admin user if it doesn't exist
        if (!userService.existsByUsername("admin")) {
            UserRegistrationDto adminDto = new UserRegistrationDto();
            adminDto.setUsername("admin");
            adminDto.setEmail("admin@example.com");
            adminDto.setPassword("admin");
            adminDto.setFullName("Administrator");
            
            User admin = userService.save(adminDto);
            admin.addRole(adminRole);
            userService.save(adminDto);
        }
    }
} 