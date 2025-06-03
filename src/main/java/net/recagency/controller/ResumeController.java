package net.recagency.controller;

import net.recagency.model.Resume;
import net.recagency.model.User;
import net.recagency.service.ResumeService;
import net.recagency.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/resume")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String showResumeForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByUsername(userDetails.getUsername());
        Resume resume = resumeService.getResumeByUser(user);
        model.addAttribute("resume", resume);
        return "resume/form";
    }

    @PostMapping
    public String saveResume(@ModelAttribute Resume resume, 
                           @AuthenticationPrincipal UserDetails userDetails,
                           Model model) {
        User user = userService.findByUsername(userDetails.getUsername());
        try {
            // Check if this is an update or new resume
            Resume existingResume = resumeService.getResumeByUser(user);
            if (existingResume.getId() != null) {
                // This is an update
                resume.setId(existingResume.getId());
                resumeService.updateResume(resume, user);
            } else {
                // This is a new resume
                resumeService.saveResume(resume, user);
            }
            return "redirect:/resume?success";
        } catch (RuntimeException e) {
            model.addAttribute("error", "Ошибка при сохранении резюме: " + e.getMessage());
            model.addAttribute("resume", resume);
            return "resume/form";
        }
    }
} 