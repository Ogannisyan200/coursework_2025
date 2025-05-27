package net.recagency.service;

import net.recagency.model.VacancyApplication;
import net.recagency.model.User;
import net.recagency.model.Vacancy;
import java.util.List;

public interface VacancyApplicationService {
    long count();
    long countByVacancy(Vacancy vacancy);
    List<VacancyApplication> getApplicationsByUser(User user);
    List<VacancyApplication> getApplicationsByVacancy(Vacancy vacancy);
    VacancyApplication applyForVacancy(Vacancy vacancy, User user);
    void markAsViewed(Long applicationId);
    boolean hasApplied(User user, Vacancy vacancy);
    VacancyApplication getApplicationById(Long id);
    void acceptApplication(Long id);
    void rejectApplication(Long id);
    List<VacancyApplication> getProcessedApplications();
    List<VacancyApplication> getProcessedApplicationsByUser(User user);
} 