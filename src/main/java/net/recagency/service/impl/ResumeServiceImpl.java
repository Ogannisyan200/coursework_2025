package net.recagency.service.impl;

import net.recagency.model.Resume;
import net.recagency.model.User;
import net.recagency.repository.ResumeRepository;
import net.recagency.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ResumeServiceImpl implements ResumeService {

    @Autowired
    private ResumeRepository resumeRepository;

    @Override
    public Resume getResumeByUser(User user) {
        return resumeRepository.findByUser(user)
                .orElse(new Resume());
    }

    @Override
    @Transactional
    public Resume saveResume(Resume resume, User user) {
        // Check if user already has a resume
        if (resumeRepository.findByUser(user).isPresent()) {
            throw new RuntimeException("User already has a resume. Please update existing resume instead.");
        }
        
        resume.setUser(user);
        resume.setCreatedAt(LocalDateTime.now());
        resume.setUpdatedAt(LocalDateTime.now());
        return resumeRepository.save(resume);
    }

    @Override
    @Transactional
    public Resume updateResume(Resume resume, User user) {
        Resume existingResume = resumeRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Resume not found for user"));
        
        // Update only the content and updatedAt fields
        existingResume.setContent(resume.getContent());
        existingResume.setUpdatedAt(LocalDateTime.now());
        
        // Merge the existing resume with the updated content
        return resumeRepository.save(existingResume);
    }
} 