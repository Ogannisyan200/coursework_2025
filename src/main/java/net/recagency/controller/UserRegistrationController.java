package net.recagency.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.recagency.dto.UserRegistrationDto;
import net.recagency.service.UserService;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

    private final UserService userService;

    @Autowired
    public UserRegistrationController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm() {
        return "registration";
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto, Model model) {
        // Check if username already exists
        if (userService.existsByUsername(registrationDto.getUsername())) {
            model.addAttribute("usernameError", "Username already exists");
            return "registration";
        }

        // Check if email already exists
        if (userService.existsByEmail(registrationDto.getEmail())) {
            model.addAttribute("emailError", "Email already exists");
            return "registration";
        }

        userService.save(registrationDto);
        return "redirect:/registration?success";
    }
} 