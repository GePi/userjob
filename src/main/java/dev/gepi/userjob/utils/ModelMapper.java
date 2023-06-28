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
    static public UserJobDTO.Users toDto(Users user) {
        if (user == null) {
            return null;
        }
        UserJobDTO.Users userDto = new UserJobDTO.Users();
        userDto.setUserId(user.getId());
        userDto.setDescription(user.getDescription());
        userDto.setIsBlocked(user.getIsBlocked());
        userDto.setBirthday(user.getBirthday());
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
        companyDto.setDescription(company.getDescription());
        return companyDto;
    }

    static public UserJobDTO.UserJobInfo toDto(UserJobInfo userJobInfo) {
        if (userJobInfo == null) {
            return null;
        }
        UserJobDTO.UserJobInfo jobInfoDto = new UserJobDTO.UserJobInfo();
        jobInfoDto.setDescription(userJobInfo.getDescription());
        jobInfoDto.setIsActivity(userJobInfo.getIsActivity());
        return jobInfoDto;
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

    public static UserJobDTO createUserJobDTO(Users user, Company company, UserJobInfo userJobInfo) {
        UserJobDTO userJobDTOResult = new UserJobDTO();

        userJobDTOResult.setUsers(ModelMapper.toDto(user));
        userJobDTOResult.setCompany(ModelMapper.toDto(company));
        userJobDTOResult.setUserJobInfo(ModelMapper.toDto(userJobInfo));
        return userJobDTOResult;
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

    public static void toModel(UserJobDTO userJobDTO, Users user, Company company, UserJobInfo userJobInfo) {
        ModelMapper.toModel(userJobDTO.getUsers(), user);
        ModelMapper.toModel(userJobDTO.getCompany(), company);
        ModelMapper.toModel(userJobDTO.getUserJobInfo(), userJobInfo);
    }

    public static void setUpdated(OffsetDateTime updatedAt, Users user, Company company, UserJobInfo userJobInfo) {
        user.setUpdated(updatedAt);
        company.setUpdated(updatedAt);
        userJobInfo.setUpdated(updatedAt);
    }

    public static void setCreated(OffsetDateTime createdAt, Users user, Company company, UserJobInfo userJobInfo) {
        user.setCreated(createdAt);
        company.setCreated(createdAt);
        userJobInfo.setCreated(createdAt);
    }
}
