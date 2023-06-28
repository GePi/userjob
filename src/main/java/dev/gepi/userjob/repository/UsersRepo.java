package dev.gepi.userjob.repository;

import dev.gepi.userjob.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepo extends JpaRepository<Users, Long> {
    Optional<Users> findById(Long userId);

    @Query("FROM Users AS usr INNER JOIN UserJobInfo AS uji on usr.id = uji.users.id WHERE uji.company.id = ?1 and uji.isActivity = true")
    List<Users> FindAllCompanyUsers(Long companyId);
}
