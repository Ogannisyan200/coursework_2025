package net.recagency.controller;

import net.recagency.service.VacancyService;
import net.recagency.service.VacancyApplicationService;
import net.recagency.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

@Controller
public class HomeController {

    @Autowired
    private VacancyService vacancyService;

    @Autowired
    private VacancyApplicationService applicationService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(Model model) {
        // Get total statistics
        long totalVacancies = vacancyService.count();
        long totalApplications = applicationService.count();
        long totalUsers = userService.count();
        
        // Get vacancy statistics with application counts
        List<Map<String, Object>> vacancyStats = vacancyService.findAll().stream()
            .map(vacancy -> {
                Map<String, Object> stats = new HashMap<>();
                stats.put("title", vacancy.getTitle());
                stats.put("applications", applicationService.countByVacancy(vacancy));
                return stats;
            })
            .collect(Collectors.toList());
        
        model.addAttribute("totalVacancies", totalVacancies);
        model.addAttribute("totalApplications", totalApplications);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("vacancyStats", vacancyStats);
        
        return "home";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
} 