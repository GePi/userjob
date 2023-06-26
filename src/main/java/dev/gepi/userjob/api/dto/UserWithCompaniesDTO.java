package dev.gepi.userjob.api.dto;

import dev.gepi.userjob.api.UserJobRequestDTO;
import lombok.Data;

import java.util.List;

@Data
public class UserWithCompaniesDTO {
    private UserJobRequestDTO.Users user;
    private List<UserJobRequestDTO.Company> companies;
}
