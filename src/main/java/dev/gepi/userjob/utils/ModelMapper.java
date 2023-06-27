package dev.gepi.userjob.utils;

import dev.gepi.userjob.api.dto.CompanyWithUsersDTO;
import dev.gepi.userjob.api.dto.UserJobDTO;
import dev.gepi.userjob.api.dto.UserWithCompaniesDTO;
import dev.gepi.userjob.model.Company;
import dev.gepi.userjob.model.UserJobInfo;
import dev.gepi.userjob.model.Users;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ModelMapper {
    static public void map(UserJobDTO userJobDTO, UserJobInfo userJobInfo) {
        userJobInfo.setDescription(userJobDTO.getUserJobInfo().getDescription());
        userJobInfo.setIsActivity(userJobDTO.getUserJobInfo().getIsActivity());
        userJobInfo.setCreated(OffsetDateTime.now());
    }

    static public void map(UserJobDTO userJobDTO, Company company) {
        company.setCompanyName(userJobDTO.getCompany().getCompanyName());
        company.setDescription(userJobDTO.getCompany().getDescription());
        company.setIsActivity(userJobDTO.getCompany().getIsActivity());
        company.setCreated(OffsetDateTime.now());
    }

    static public void map(UserJobDTO userJobDTO, Users user) {
        user.setFamilyName(userJobDTO.getUsers().getFamilyName());
        user.setFirstName(userJobDTO.getUsers().getFirstName());
        user.setMiddleName(userJobDTO.getUsers().getMiddleName());
        user.setCreated(OffsetDateTime.now());
    }

    static public UserJobDTO.Users toDto(Users user) {
        if (user == null) {
            return null;
        }
        UserJobDTO.Users userDto = new UserJobDTO.Users();
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

    static public UserJobDTO.Company toDto(Company company) {
        if (company == null) {
            return null;
        }
        UserJobDTO.Company companyDto = new UserJobDTO.Company();
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

    static public CompanyWithUsersDTO toCompanyWithUsersDTO(Company company, List<Users> users) {
        CompanyWithUsersDTO result = new CompanyWithUsersDTO();
        result.setCompany(toDto(company));
        result.setUsers(users.stream().map(ModelMapper::toDto).collect(Collectors.toList()));
        return result;
    }

    public static void toModel(UserJobDTO.Users userDto, Users user) {
        user.setMiddleName(userDto.getMiddleName());
        user.setFirstName(userDto.getFirstName());
        user.setFamilyName(userDto.getFamilyName());
        user.setDescription(userDto.getDescription());
        user.setGender(userDto.getGender());
        user.setBirthday(userDto.getBirthday());
        user.setAge(userDto.getAge());
        user.setIsBlocked(userDto.getIsBlocked());
    }

    public static void toModel(UserJobDTO.Company companyDto, Company company) {
        company.setCompanyName(companyDto.getCompanyName());
        company.setDescription(companyDto.getDescription());
        company.setIsActivity(companyDto.getIsActivity());
    }

    public static void toModel(UserJobDTO.UserJobInfo userJobInfoDto, UserJobInfo userJobInfo) {
        userJobInfo.setDescription(userJobInfoDto.getDescription());
        userJobInfo.setIsActivity(userJobInfoDto.getIsActivity());
    }
}
