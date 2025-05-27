package net.recagency.controller;

import net.recagency.model.User;
import net.recagency.model.VacancyApplication;
import net.recagency.service.UserService;
import net.recagency.service.VacancyApplicationService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Controller
@RequestMapping("/applications")
public class ApplicationHistoryController {

    @Autowired
    private VacancyApplicationService applicationService;

    @Autowired
    private UserService userService;

    @GetMapping("/history")
    @Transactional(readOnly = true)
    public String viewHistory(Authentication authentication, Model model) {
        User user = userService.findByUsername(authentication.getName());
        List<VacancyApplication> applications;
        
        if (user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"))) {
            // Для админа показываем все принятые и отклоненные заявки
            applications = applicationService.getProcessedApplications();
        } else {
            // Для обычного пользователя показываем только его заявки
            applications = applicationService.getProcessedApplicationsByUser(user);
        }
        
        // Initialize user and vacancy for each application
        for (VacancyApplication app : applications) {
            Hibernate.initialize(app.getUser());
            Hibernate.initialize(app.getVacancy());
        }
        
        model.addAttribute("applications", applications);
        return "applications/history";
    }
}