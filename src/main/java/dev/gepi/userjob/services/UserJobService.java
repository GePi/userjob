package dev.gepi.userjob.services;

import dev.gepi.userjob.model.Company;
import dev.gepi.userjob.model.Users;
import dev.gepi.userjob.repository.CompanyRepo;
import dev.gepi.userjob.repository.UsersRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserJobService {
    private final UsersRepo usersRepo;
    private final CompanyRepo companyRepo;

    public UserJobService(UsersRepo usersRepo, CompanyRepo companyRepo) {
        this.usersRepo = usersRepo;
        this.companyRepo = companyRepo;
    }

    public Users getUserById(Long userId) {
        return usersRepo.findById(userId).orElse(null);
    }

    public Company getCompanyById(Long companyId) {
        return companyRepo.findById(companyId).orElse(null);
    }

    public List<Company> getCompaniesByUserId(Long userId) {
        return companyRepo.FindAllUserCompanies(userId);
    }

    public List<Users> getUsersByCompanyId(Long companyId) {
        return usersRepo.FindAllCompanyUsers(companyId);
    }

    @Transactional
    public void save(Company company, Users user) {
        usersRepo.save(user);
        companyRepo.save(company);
    }
}
