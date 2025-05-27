package net.recagency.repository;

import net.recagency.model.VacancyApplication;
import net.recagency.model.Vacancy;
import net.recagency.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VacancyApplicationRepository extends JpaRepository<VacancyApplication, Long> {
    List<VacancyApplication> findByUser(User user);
    List<VacancyApplication> findByVacancy(Vacancy vacancy);
    long countByVacancy(Vacancy vacancy);
    boolean existsByUserAndVacancy(User user, Vacancy vacancy);
} 