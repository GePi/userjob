package dev.gepi.userjob.api;

import dev.gepi.userjob.api.dto.ModelMapper;
import dev.gepi.userjob.api.dto.UserJobDTO;
import dev.gepi.userjob.model.Company;
import dev.gepi.userjob.model.UserJobInfo;
import dev.gepi.userjob.model.Users;
import dev.gepi.userjob.services.UserJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class UserJobController {

    private UserJobService userJobService;

    public UserJobController(UserJobService userJobService) {
        this.userJobService = userJobService;
    }

    @PostMapping("create-userjob")
    public ResponseEntity<Void> postUserJob(@RequestBody UserJobDTO userJobDTO) {
        Users user = getUser(userJobDTO);
        Company company = getCompany(userJobDTO);

        if (company == null || user == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        UserJobInfo userJobInfo = getUserJobInfo(userJobDTO, user, company);
        if (userJobInfo.getId() != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        company.addUserJobInfo(userJobInfo);
        user.addUserJobInfo(userJobInfo);

        userJobService.save(company, user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("update-userjob")
    public ResponseEntity<UserJobDTO> patchUserJob(@RequestBody UserJobDTO userJobDTO) {
        UserJobDTO result;
        Users user = userJobService.getUserById(userJobDTO.getUsers().getUserId());
        Company company = userJobService.getCompanyById(userJobDTO.getCompany().getIdCompany());

        if (user == null || company == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        UserJobInfo userJobInfo = user.getUserJobInfoList()
                .stream()
                .filter(jobInfo -> jobInfo.getCompany().getId().equals(company.getId()))
                .findFirst()
                .orElse(null);
        if (userJobInfo == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        OffsetDateTime now = OffsetDateTime.now();
        ModelMapper.toModel(userJobDTO.getUsers(), user);
        ModelMapper.toModel(userJobDTO.getCompany(), company);
        ModelMapper.toModel(userJobDTO.getUserJobInfo(), userJobInfo);
        user.setUpdated(now);
        company.setUpdated(now);
        userJobInfo.setUpdated(now);

        userJobService.save(company, user);
        result = userJobDTO;
        return ResponseEntity.ok(result);
    }

    @GetMapping("get-userjob")
    public ResponseEntity<?> getUserJob(UserJobDTO.Users userParam,
                                        UserJobDTO.Company companyParam) {

        if (userParam.getUserId() == null && companyParam.getIdCompany() == null) {
            return ResponseEntity.badRequest().build();
        }

        if (userParam.getUserId() != null) {
            Users user = userJobService.getUserById(userParam.getUserId());
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            List<Company> companies = userJobService.getCompaniesByUserId(userParam.getUserId());
            return ResponseEntity.ok(ModelMapper.toUserWithCompaniesDTO(user, companies));
        }

        if (companyParam.getIdCompany() != null) {
            Company company = userJobService.getCompanyById(companyParam.getIdCompany());
            List<Users> users = userJobService.getUsersByCompanyId(companyParam.getIdCompany());
            return ResponseEntity.ok(ModelMapper.toCompanyWithUsersDTO(company, users));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private UserJobInfo getUserJobInfo(UserJobDTO userJobDTO, Users user, Company company) {
        UserJobInfo userJobInfo;
        if (user.getId() != null && company.getId() != null) {
            userJobInfo = userJobService.getUserJobInfoByUserIdAndCompanyId(user.getId(), company.getId());
            if (userJobInfo != null) {
                return userJobInfo;
            }
        }
        userJobInfo = new UserJobInfo();
        ModelMapper.map(userJobDTO, userJobInfo);
        return userJobInfo;
    }

    private Company getCompany(UserJobDTO userJobDTO) {
        Company company;
        if (userJobDTO.getUserJobInfo().getIdCompany() == null) {
            company = new Company();
            ModelMapper.map(userJobDTO, company);
        } else {
            company = userJobService.getCompanyById(userJobDTO.getUserJobInfo().getIdCompany());
        }
        return company;
    }

    private Users getUser(UserJobDTO userJobDTO) {
        Users user;
        if (userJobDTO.getUserJobInfo().getUserId() == null) {
            user = new Users();
            ModelMapper.map(userJobDTO, user);
        } else {
            user = userJobService.getUserById(userJobDTO.getUserJobInfo().getUserId());
        }
        return user;
    }
}
