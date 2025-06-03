package net.recagency.service;

import net.recagency.dto.UserRegistrationDto;
import net.recagency.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    long count();
    User save(UserRegistrationDto registrationDto);
    User saveUser(User user);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    User findByUsername(String username);
    List<User> getAllUsers();
    void deleteUser(Long id);
} 