package net.recagency.service;

import net.recagency.model.Vacancy;
import net.recagency.model.User;
import java.util.List;

public interface VacancyService {
    long count();
    List<Vacancy> findAll();
    List<Vacancy> getAllVacancies();
    Vacancy getVacancyById(Long id);
    Vacancy saveVacancy(Vacancy vacancy, User user);
    void deleteVacancy(Long id);
    Vacancy updateVacancy(Long id, Vacancy vacancy);
} 