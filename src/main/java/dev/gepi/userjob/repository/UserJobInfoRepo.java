package dev.gepi.userjob.repository;

import dev.gepi.userjob.model.Company;
import dev.gepi.userjob.model.UserJobInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserJobInfoRepo extends JpaRepository<UserJobInfo, Long> {
    Optional<UserJobInfo> findById(Long userJobInfoId);

    Optional<UserJobInfo> findFirstByUsersIdAndAndCompanyId(Long userId, Long idCompany);
}
