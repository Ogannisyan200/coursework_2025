package net.recagency.service.impl;

import net.recagency.model.VacancyApplication;
import net.recagency.model.User;
import net.recagency.model.Vacancy;
import net.recagency.repository.VacancyApplicationRepository;
import net.recagency.service.VacancyApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VacancyApplicationServiceImpl implements VacancyApplicationService {

    @Autowired
    private VacancyApplicationRepository applicationRepository;

    @Override
    public long count() {
        return applicationRepository.count();
    }

    @Override
    public long countByVacancy(Vacancy vacancy) {
        return applicationRepository.countByVacancy(vacancy);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VacancyApplication> getApplicationsByUser(User user) {
        return applicationRepository.findByUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VacancyApplication> getApplicationsByVacancy(Vacancy vacancy) {
        return applicationRepository.findByVacancy(vacancy);
    }

    @Override
    @Transactional
    public VacancyApplication applyForVacancy(Vacancy vacancy, User user) {
        if (applicationRepository.existsByUserAndVacancy(user, vacancy)) {
            return null;
        }
        VacancyApplication application = new VacancyApplication();
        application.setVacancy(vacancy);
        application.setUser(user);
        return applicationRepository.save(application);
    }

    @Override
    @Transactional
    public void markAsViewed(Long applicationId) {
        VacancyApplication application = applicationRepository.findById(applicationId).orElse(null);
        if (application != null) {
            application.setViewed(true);
            applicationRepository.save(application);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasApplied(User user, Vacancy vacancy) {
        return applicationRepository.existsByUserAndVacancy(user, vacancy);
    }

    @Override
    @Transactional(readOnly = true)
    public VacancyApplication getApplicationById(Long id) {
        return applicationRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void acceptApplication(Long id) {
        VacancyApplication application = applicationRepository.findById(id).orElse(null);
        if (application != null) {
            application.setStatus(VacancyApplication.ApplicationStatus.ACCEPTED);
            applicationRepository.save(application);
        }
    }

    @Override
    @Transactional
    public void rejectApplication(Long id) {
        VacancyApplication application = applicationRepository.findById(id).orElse(null);
        if (application != null) {
            application.setStatus(VacancyApplication.ApplicationStatus.REJECTED);
            applicationRepository.save(application);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<VacancyApplication> getProcessedApplications() {
        return applicationRepository.findAll().stream()
            .filter(app -> app.getStatus() != VacancyApplication.ApplicationStatus.PENDING)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VacancyApplication> getProcessedApplicationsByUser(User user) {
        return applicationRepository.findByUser(user).stream()
            .filter(app -> app.getStatus() != VacancyApplication.ApplicationStatus.PENDING)
            .collect(Collectors.toList());
    }
} 