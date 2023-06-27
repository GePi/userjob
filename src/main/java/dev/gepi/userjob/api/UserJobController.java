package dev.gepi.userjob.api;

import dev.gepi.userjob.utils.EntityComparatorUtil;
import dev.gepi.userjob.utils.ModelMapper;
import dev.gepi.userjob.api.dto.UserJobDTO;
import dev.gepi.userjob.model.Company;
import dev.gepi.userjob.model.UserJobInfo;
import dev.gepi.userjob.model.Users;
import dev.gepi.userjob.services.UserJobService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
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
        if (userJobDTO == null || userJobDTO.getUsers() == null || userJobDTO.getCompany() == null || userJobDTO.getUserJobInfo() == null) {
            return ResponseEntity.badRequest().build();
        }

        Users user = getUser(userJobDTO.getUsers());
        Company company = getCompany(userJobDTO.getCompany());

        if (company.getId() != null || user.getId() != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        UserJobInfo userJobInfo = getUserJobInfo(userJobDTO.getUserJobInfo());

        company.addUserJobInfo(userJobInfo);
        user.addUserJobInfo(userJobInfo);

        userJobService.save(company, user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("update-userjob")
    public ResponseEntity<List<String>> patchUserJob(@RequestBody UserJobDTO userJobDTO) throws IllegalAccessException {

        if (userJobDTO == null || userJobDTO.getUsers() == null || userJobDTO.getCompany() == null || userJobDTO.getUserJobInfo() == null) {
            return ResponseEntity.badRequest().build();
        }

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

        Users userPrevState = new Users(user);
        Company companyPrevState = new Company(company);
        UserJobInfo userJobInfoPrevState = new UserJobInfo(userJobInfo);

        ModelMapper.toModel(userJobDTO.getUsers(), user);
        ModelMapper.toModel(userJobDTO.getCompany(), company);
        ModelMapper.toModel(userJobDTO.getUserJobInfo(), userJobInfo);
        OffsetDateTime now = OffsetDateTime.now();
        user.setUpdated(now);
        company.setUpdated(now);
        userJobInfo.setUpdated(now);

        userJobService.save(company, user);

        List<String> resultList = new ArrayList<>();
        resultList.addAll(EntityComparatorUtil.getDifferentFields(user, userPrevState));
        resultList.addAll(EntityComparatorUtil.getDifferentFields(company, companyPrevState));
        resultList.addAll(EntityComparatorUtil.getDifferentFields(userJobInfo, userJobInfoPrevState));

        return ResponseEntity.ok(resultList);
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

    private UserJobInfo getUserJobInfo(@NonNull UserJobDTO.UserJobInfo userJobInfoDto) {
        UserJobInfo userJobInfo = new UserJobInfo();
        ModelMapper.toModel(userJobInfoDto, userJobInfo);
        return userJobInfo;
    }

    private Company getCompany(@NonNull UserJobDTO.Company companyDto) {
        Company company = null;
        if (companyDto.getIdCompany() != null) {
            company = userJobService.getCompanyById(companyDto.getIdCompany());
        }
        if (company == null) {
            company = new Company();
            ModelMapper.toModel(companyDto, company);
        }
        return company;
    }

    private Users getUser(@NonNull UserJobDTO.Users userDto) {
        Users user = null;
        if (userDto.getUserId() != null) {
            user = userJobService.getUserById(userDto.getUserId());
        }
        if (user == null) {
            user = new Users();
            ModelMapper.toModel(userDto, user);
        }
        return user;
    }
}
