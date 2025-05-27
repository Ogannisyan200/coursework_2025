package net.recagency.controller;

import net.recagency.model.Vacancy;
import net.recagency.model.User;
import net.recagency.service.VacancyService;
import net.recagency.service.UserService;
import net.recagency.service.VacancyApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.Hibernate;

@Controller
@RequestMapping("/vacancies")
public class VacancyController {

    @Autowired
    private VacancyService vacancyService;

    @Autowired
    private UserService userService;

    @Autowired
    private VacancyApplicationService applicationService;

    @GetMapping
    @Transactional(readOnly = true)
    public String listVacancies(Model model) {
        model.addAttribute("vacancies", vacancyService.getAllVacancies());
        return "vacancies/list";
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public String viewVacancy(@PathVariable Long id, 
                            @AuthenticationPrincipal UserDetails userDetails,
                            Model model) {
        Vacancy vacancy = vacancyService.getVacancyById(id);
        Hibernate.initialize(vacancy.getUser());
        model.addAttribute("vacancy", vacancy);
        
        if (userDetails != null) {
            User user = userService.findByUsername(userDetails.getUsername());
            boolean hasApplied = applicationService.hasApplied(user, vacancy);
            model.addAttribute("hasApplied", hasApplied);
        }
        
        return "vacancies/view";
    }

    @GetMapping("/new")
    @Transactional(readOnly = true)
    public String showCreateForm(Model model) {
        model.addAttribute("vacancy", new Vacancy());
        return "vacancies/form";
    }

    @PostMapping
    @Transactional
    public String createVacancy(@ModelAttribute Vacancy vacancy, 
                              @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        vacancyService.saveVacancy(vacancy, user);
        return "redirect:/vacancies";
    }

    @GetMapping("/{id}/edit")
    @Transactional(readOnly = true)
    public String showEditForm(@PathVariable Long id, Model model) {
        Vacancy vacancy = vacancyService.getVacancyById(id);
        Hibernate.initialize(vacancy.getUser());
        model.addAttribute("vacancy", vacancy);
        return "vacancies/form";
    }

    @PostMapping("/{id}")
    @Transactional
    public String updateVacancy(@PathVariable Long id, @ModelAttribute Vacancy vacancy) {
        vacancyService.updateVacancy(id, vacancy);
        return "redirect:/vacancies";
    }

    @PostMapping("/{id}/delete")
    @Transactional
    public String deleteVacancy(@PathVariable Long id) {
        vacancyService.deleteVacancy(id);
        return "redirect:/vacancies";
    }
} 