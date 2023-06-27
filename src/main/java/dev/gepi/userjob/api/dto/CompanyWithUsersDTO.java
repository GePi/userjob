package dev.gepi.userjob.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class CompanyWithUsersDTO {
    private UserJobDTO.Company company;
    private List<UserJobDTO.Users> users;
}
