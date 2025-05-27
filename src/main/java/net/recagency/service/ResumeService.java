package net.recagency.service;

import net.recagency.model.Resume;
import net.recagency.model.User;

public interface ResumeService {
    Resume getResumeByUser(User user);
    Resume saveResume(Resume resume, User user);
    Resume updateResume(Resume resume, User user);
} 