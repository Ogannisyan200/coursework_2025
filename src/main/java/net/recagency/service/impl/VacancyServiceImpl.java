package net.recagency.service.impl;

import net.recagency.model.Vacancy;
import net.recagency.model.User;
import net.recagency.repository.VacancyRepository;
import net.recagency.service.VacancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VacancyServiceImpl implements VacancyService {

    @Autowired
    private VacancyRepository vacancyRepository;

    @Override
    public long count() {
        return vacancyRepository.count();
    }

    @Override
    public List<Vacancy> findAll() {
        return vacancyRepository.findAll();
    }

    @Override
    public List<Vacancy> getAllVacancies() {
        return vacancyRepository.findAll();
    }

    @Override
    public Vacancy getVacancyById(Long id) {
        return vacancyRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Vacancy saveVacancy(Vacancy vacancy, User user) {
        vacancy.setUser(user);
        return vacancyRepository.save(vacancy);
    }

    @Override
    @Transactional
    public void deleteVacancy(Long id) {
        vacancyRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Vacancy updateVacancy(Long id, Vacancy vacancy) {
        Vacancy existingVacancy = vacancyRepository.findById(id).orElse(null);
        if (existingVacancy != null) {
            existingVacancy.setTitle(vacancy.getTitle());
            existingVacancy.setDescription(vacancy.getDescription());
            existingVacancy.setRequirements(vacancy.getRequirements());
            existingVacancy.setSalary(vacancy.getSalary());
            existingVacancy.setCompany(vacancy.getCompany());
            existingVacancy.setLocation(vacancy.getLocation());
            return vacancyRepository.save(existingVacancy);
        }
        return null;
    }
} 