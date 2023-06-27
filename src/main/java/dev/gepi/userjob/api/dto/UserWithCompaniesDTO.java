package dev.gepi.userjob.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserWithCompaniesDTO {
    private UserJobDTO.Users user;
    private List<UserJobDTO.Company> companies;
}
