package dev.gepi.userjob.api;

import dev.gepi.userjob.api.dto.UserWithCompaniesDTO;
import dev.gepi.userjob.model.Company;
import dev.gepi.userjob.model.UserJobInfo;
import dev.gepi.userjob.model.Users;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ModelMapper {
    static public void map(UserJobRequestDTO userJobRequestDTO, UserJobInfo userJobInfo) {
        userJobInfo.setDescription(userJobRequestDTO.getUserJobInfo().getDescription());
        userJobInfo.setIsActivity(userJobRequestDTO.getUserJobInfo().getIsActivity());
        userJobInfo.setCreated(OffsetDateTime.now());
    }

    static public void map(UserJobRequestDTO userJobRequestDTO, Company company) {
        company.setCompanyName(userJobRequestDTO.getCompany().getCompanyName());
        company.setDescription(userJobRequestDTO.getCompany().getDescription());
        company.setIsActivity(userJobRequestDTO.getCompany().getIsActivity());
        company.setCreated(OffsetDateTime.now());
    }

    static public void map(UserJobRequestDTO userJobRequestDTO, Users user) {
        user.setFamilyName(userJobRequestDTO.getUsers().getFamilyName());
        user.setFirstName(userJobRequestDTO.getUsers().getFirstName());
        user.setMiddleName(userJobRequestDTO.getUsers().getMiddleName());
        user.setCreated(OffsetDateTime.now());
    }

    static public UserJobRequestDTO.Users toDto(Users user) {
        if (user == null) {
            return null;
        }
        UserJobRequestDTO.Users userDto = new UserJobRequestDTO.Users();
        userDto.setUserId(user.getId());
        userDto.setDescription(user.getDescription());
        userDto.setIsBlocked(user.getIsBlocked());
        userDto.setAge(user.getAge());
        userDto.setFirstName(user.getFirstName());
        userDto.setMiddleName(user.getMiddleName());
        userDto.setFamilyName(user.getFamilyName());
        userDto.setGender(user.getGender());
        return userDto;
    }

    static public UserJobRequestDTO.Company toDto(Company company) {
        if (company == null) {
            return null;
        }
        UserJobRequestDTO.Company companyDto = new UserJobRequestDTO.Company();
        companyDto.setIdCompany(company.getId());
        companyDto.setCompanyName(company.getCompanyName());
        companyDto.setIsActivity(company.getIsActivity());
        return companyDto;
    }

    static public UserWithCompaniesDTO toUserWithCompaniesDTO(Users user, List<Company> companies) {
        UserWithCompaniesDTO result = new UserWithCompaniesDTO();
        result.setUser(toDto(user));
        result.setCompanies(companies.stream().map(ModelMapper::toDto).collect(Collectors.toList()));
        return result;
    }
}
