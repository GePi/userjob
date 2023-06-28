package dev.gepi.userjob.api;

import dev.gepi.userjob.utils.DTOComparatorUtil;
import dev.gepi.userjob.utils.ModelMapper;
import dev.gepi.userjob.api.dto.UserJobDTO;
import dev.gepi.userjob.model.Company;
import dev.gepi.userjob.model.UserJobInfo;
import dev.gepi.userjob.model.Users;
import dev.gepi.userjob.services.UserJobService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class UserJobController {

    private final UserJobService userJobService;

    public UserJobController(UserJobService userJobService) {
        this.userJobService = userJobService;
    }

    @PostMapping("create-userjob")
    public ResponseEntity<Void> createUserJob(@RequestBody UserJobDTO userJobDTO) {
        if (userJobDTO == null || userJobDTO.getUsers() == null || userJobDTO.getCompany() == null || userJobDTO.getUserJobInfo() == null) {
            log.error("postUserJob NULL {}", userJobDTO);
            return ResponseEntity.badRequest().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
        }

        Users user = getExistOrCreateUser(userJobDTO.getUsers());
        Company company = getExistOrCreateCompany(userJobDTO.getCompany());

        if (company.getId() != null || user.getId() != null) {
            log.error("postUserJob CONFLICT {} {} {}", userJobDTO, company, user);
            return ResponseEntity.status(HttpStatus.CONFLICT).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
        }

        UserJobInfo userJobInfo = createUserJobInfo(userJobDTO.getUserJobInfo());
        ModelMapper.setCreated(OffsetDateTime.now(), user, company, userJobInfo);

        company.addUserJobInfo(userJobInfo);
        user.addUserJobInfo(userJobInfo);

        userJobService.save(company, user);
        return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
    }

    @PatchMapping("update-userjob")
    public ResponseEntity<List<String>> updateUserJob(@RequestBody UserJobDTO userJobDTO) throws IllegalAccessException {
        if (userJobDTO == null || userJobDTO.getUsers() == null || userJobDTO.getCompany() == null || userJobDTO.getUserJobInfo() == null) {
            log.error("patchUserJob NULL {}", userJobDTO);
            return ResponseEntity.badRequest().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
        }

        Users user = userJobService.getUserById(userJobDTO.getUsers().getUserId());
        Company company = userJobService.getCompanyById(userJobDTO.getCompany().getIdCompany());

        if (user == null || company == null) {
            log.error("patchUserJob NULL {} user {} company {}", userJobDTO, user, company);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
        }

        UserJobInfo userJobInfo = user.getUserJobInfoList()
                .stream()
                .filter(jobInfo -> jobInfo.getCompany().getId().equals(company.getId()))
                .findFirst()
                .orElse(null);
        if (userJobInfo == null) {
            log.error("patchUserJob NULL userJobInfo {} {}", user, company);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
        }

        UserJobDTO prevStateUserJobDto = ModelMapper.createUserJobDTO(user, company, userJobInfo);

        ModelMapper.toModel(userJobDTO, user, company, userJobInfo);
        ModelMapper.setUpdated(OffsetDateTime.now(), user, company, userJobInfo);
        userJobService.save(company, user);

        UserJobDTO curStateUserJobDto = ModelMapper.createUserJobDTO(user, company, userJobInfo);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body(compareByFields(curStateUserJobDto, prevStateUserJobDto));
    }

    @GetMapping("get-userjob")
    public ResponseEntity<?> getUserJob(UserJobDTO.Users userParam,
                                        UserJobDTO.Company companyParam) {

        if (userParam.getUserId() == null && companyParam.getIdCompany() == null) {
            log.error("getUserJob NULL all input params");
            return ResponseEntity.notFound().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
        }

        if (userParam.getUserId() != null) {
            Users user = userJobService.getUserById(userParam.getUserId());
            if (user == null) {
                log.error("getUserJob NOT_FOUND user {}", userParam.getUserId());
                return ResponseEntity.notFound().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
            }
            List<Company> companies = userJobService.getCompaniesByUserId(userParam.getUserId());
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(ModelMapper.toUserWithCompaniesDTO(user, companies));
        }

        if (companyParam.getIdCompany() != null) {
            Company company = userJobService.getCompanyById(companyParam.getIdCompany());
            if (company == null) {
                log.error("getUserJob NOT_FOUND company {}", companyParam.getIdCompany());
                return ResponseEntity.notFound().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
            }
            List<Users> users = userJobService.getUsersByCompanyId(companyParam.getIdCompany());
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(ModelMapper.toCompanyWithUsersDTO(company, users));
        } else {
            log.error("getUserJob NULL all input params");
            return ResponseEntity.notFound().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
        }
    }

    private UserJobInfo createUserJobInfo(@NonNull UserJobDTO.UserJobInfo userJobInfoDto) {
        UserJobInfo userJobInfo = new UserJobInfo();
        ModelMapper.toModel(userJobInfoDto, userJobInfo);
        return userJobInfo;
    }

    private Company getExistOrCreateCompany(@NonNull UserJobDTO.Company companyDto) {
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

    private Users getExistOrCreateUser(@NonNull UserJobDTO.Users userDto) {
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

    public List<String> compareByFields(UserJobDTO userJobDTO, UserJobDTO userJobDTOResult) throws IllegalAccessException {
        List<String> resultList = new ArrayList<>();
        resultList.addAll(DTOComparatorUtil.getDifferentFields(userJobDTO.getUsers(), userJobDTOResult.getUsers()));
        resultList.addAll(DTOComparatorUtil.getDifferentFields(userJobDTO.getCompany(), userJobDTOResult.getCompany()));
        resultList.addAll(DTOComparatorUtil.getDifferentFields(userJobDTO.getUserJobInfo(), userJobDTOResult.getUserJobInfo()));
        return resultList;
    }
}
