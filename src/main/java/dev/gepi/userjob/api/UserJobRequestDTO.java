package dev.gepi.userjob.api;

import lombok.Data;

import java.util.Date;

@Data
public class UserJobRequestDTO {
    private Users users = new Users();
    private UserJobInfo userJobInfo = new UserJobInfo();
    private Company company = new Company();

    @Data
    public static class Users {
        private Long userId;
        private String familyName;
        private String middleName;
        private String firstName;
        private Date birthday;
        private String gender;
        private Integer age;
        private String description;
        private Boolean isBlocked;
    }

    @Data
    public static class UserJobInfo {
        private Long idCompany;
        private Long userId;
        private String description;
        private Boolean isActivity;
    }

    @Data
    public static class Company {
        private Long idCompany;
        private String companyName;
        private String description;
        private Boolean isActivity;
    }
}
