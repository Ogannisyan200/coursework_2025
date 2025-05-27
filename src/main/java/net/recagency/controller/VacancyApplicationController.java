package net.recagency.controller;

import net.recagency.model.Vacancy;
import net.recagency.model.User;
import net.recagency.model.VacancyApplication;
import net.recagency.service.VacancyService;
import net.recagency.service.VacancyApplicationService;
import net.recagency.service.UserService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@Controller
@RequestMapping("/applications")
public class VacancyApplicationController {

    @Autowired
    private VacancyApplicationService applicationService;

    @Autowired
    private VacancyService vacancyService;

    @Autowired
    private UserService userService;

    @PostMapping("/vacancy/{vacancyId}")
    public String applyForVacancy(@PathVariable Long vacancyId,
                                @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        Vacancy vacancy = vacancyService.getVacancyById(vacancyId);
        applicationService.applyForVacancy(vacancy, user);
        return "redirect:/vacancies/" + vacancyId + "?applied";
    }

    @GetMapping("/vacancy/{vacancyId}")
    @Transactional(readOnly = true)
    public String viewApplications(@PathVariable Long vacancyId,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 Model model) {
        User user = userService.findByUsername(userDetails.getUsername());
        Vacancy vacancy = vacancyService.getVacancyById(vacancyId);
        List<VacancyApplication> applications = applicationService.getApplicationsByVacancy(vacancy);
        
        // Initialize user entities for each application
        for (VacancyApplication app : applications) {
            Hibernate.initialize(app.getUser());
        }
        
        model.addAttribute("applications", applications);
        model.addAttribute("vacancy", vacancy);
        return "applications/list";
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public String viewApplication(@PathVariable Long id,
                                @AuthenticationPrincipal UserDetails userDetails,
                                Model model) {
        VacancyApplication application = applicationService.getApplicationById(id);
        Hibernate.initialize(application.getUser());
        Hibernate.initialize(application.getUser().getResume());
        Hibernate.initialize(application.getVacancy());
        applicationService.markAsViewed(id);
        model.addAttribute("app", application);
        return "applications/view";
    }

    @PostMapping("/{id}/accept")
    @PreAuthorize("hasRole('ADMIN')")
    public String acceptApplication(@PathVariable Long id) {
        applicationService.acceptApplication(id);
        return "redirect:/applications/" + id + "?accepted";
    }
    
    @PostMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public String rejectApplication(@PathVariable Long id) {
        applicationService.rejectApplication(id);
        return "redirect:/applications/" + id + "?rejected";
    }
} 