package dev.gepi.userjob.repository;

import dev.gepi.userjob.model.Company;
import dev.gepi.userjob.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CompanyRepo extends JpaRepository<Company, Long> {
    Optional<Company> findById(Long companyId);

    @Query("FROM Company AS cmp INNER JOIN UserJobInfo AS uji on cmp.id = uji.company.id WHERE uji.users.id = ?1 and uji.isActivity = true")
    List<Company> FindAllUserCompanies(Long userId);
}
