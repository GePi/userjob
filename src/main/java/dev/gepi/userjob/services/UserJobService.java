package dev.gepi.userjob.services;

import dev.gepi.userjob.model.Company;
import dev.gepi.userjob.model.UserJobInfo;
import dev.gepi.userjob.model.Users;
import dev.gepi.userjob.repository.CompanyRepo;
import dev.gepi.userjob.repository.UserJobInfoRepo;
import dev.gepi.userjob.repository.UsersRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserJobService {
    private final UsersRepo usersRepo;
    private final CompanyRepo companyRepo;
    private final UserJobInfoRepo userJobInfoRepo;

    public UserJobService(UsersRepo usersRepo, CompanyRepo companyRepo, UserJobInfoRepo userJobInfoRepo) {
        this.usersRepo = usersRepo;
        this.companyRepo = companyRepo;
        this.userJobInfoRepo = userJobInfoRepo;
    }

    public Users getUserById(Long userId) {
        return usersRepo.findById(userId).orElse(null);
    }

    public Company getCompanyById(Long companyId) {
        return companyRepo.findById(companyId).orElse(null);
    }

    public UserJobInfo getUserJobInfoByUserIdAndCompanyId(Long userId, Long companyId) {
        return userJobInfoRepo.findFirstByUsersIdAndAndCompanyId(userId, companyId).orElse(null);
    }

    public List<Company> getCompaniesByUserId(Long userId) {
        return companyRepo.FindAllUserCompanies(userId);
    }

    @Transactional
    public void save(Company company, Users user) {
        // TODO может быть стоит завернуть ошибки БД (при параллельном выполнении н-р)
        usersRepo.save(user);
        companyRepo.save(company);
    }
}
