package dev.gepi.userjob.api;

import dev.gepi.userjob.model.Company;
import dev.gepi.userjob.model.UserJobInfo;
import dev.gepi.userjob.model.Users;
import dev.gepi.userjob.services.UserJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Void> postUserJob(@RequestBody UserJobRequestDTO userJobRequestDTO) {
        Users user = getUsers(userJobRequestDTO);
        Company company = getCompany(userJobRequestDTO);

        if (company == null || user == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        UserJobInfo userJobInfo = getUserJobInfo(userJobRequestDTO, user, company);
        if (userJobInfo == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        company.addUserJobInfo(userJobInfo);
        user.addUserJobInfo(userJobInfo);

        userJobService.save(company, user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("XXXget-userjob")
    public ResponseEntity<List<UserJobRequestDTO>> getUserJob(@RequestParam UserJobRequestDTO.Users user,
                                                              UserJobRequestDTO.Company company) {
        UserJobRequestDTO userJobRequestDTOResult = new UserJobRequestDTO();
        //return ResponseEntity.ok(List.of(userJobRequestResult));
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("get-userjob")
    public ResponseEntity<?> getUserJob(@RequestParam(required = false) Long userId,
                                        @RequestParam(required = false) Long companyId) {
        if (userId == null && companyId == null) {
            return ResponseEntity.badRequest().build();
        }

        if (userId != null) {
            Users user = userJobService.getUserById(userId);
            List<Company> companies = userJobService.getCompaniesByUserId(userId);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            //TODO убрать
            for (var company : companies) {
                System.out.println(company.getCompanyName());
            }
            return ResponseEntity.ok(ModelMapper.toUserWithCompaniesDTO(user, companies));
        }

        UserJobRequestDTO userJobRequestDTOResult = new UserJobRequestDTO();
        //return ResponseEntity.ok(List.of(userJobRequestResult));
        return ResponseEntity.badRequest().build();
    }


    private UserJobInfo getUserJobInfo(UserJobRequestDTO userJobRequestDTO, Users user, Company company) {
        UserJobInfo userJobInfo;
        if (user.getId() != null && company.getId() != null) {
            userJobInfo = userJobService.getUserJobInfoByUserIdAndCompanyId(user.getId(), company.getId());
            if (userJobInfo != null) {
                //TODO связь уже есть
                return null;
            }
        }
        userJobInfo = new UserJobInfo();
        ModelMapper.map(userJobRequestDTO, userJobInfo);
        return userJobInfo;
    }

    private Company getCompany(UserJobRequestDTO userJobRequestDTO) {
        Company company;
        if (userJobRequestDTO.getUserJobInfo().getIdCompany() == null ||
                userJobRequestDTO.getUserJobInfo().getIdCompany() == 0) {
            company = new Company();
            ModelMapper.map(userJobRequestDTO, company);
        } else {
            company = userJobService.getCompanyById(userJobRequestDTO.getUserJobInfo().getIdCompany());
        }
        return company;
    }

    private Users getUsers(UserJobRequestDTO userJobRequestDTO) {
        Users user;
        if (userJobRequestDTO.getUserJobInfo().getUserId() == null ||
                userJobRequestDTO.getUserJobInfo().getUserId() == 0) {
            user = new Users();
            ModelMapper.map(userJobRequestDTO, user);
        } else {
            user = userJobService.getUserById(userJobRequestDTO.getUserJobInfo().getUserId());
        }
        return user;
    }


}
