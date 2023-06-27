package dev.gepi.userjob.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode
public class UserJobDTO {
    private Users users = new Users();
    private UserJobInfo userJobInfo = new UserJobInfo();
    private Company company = new Company();

    @Data
    @EqualsAndHashCode
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
    @EqualsAndHashCode
    public static class UserJobInfo {
        private String description;
        private Boolean isActivity;
    }

    @Data
    @EqualsAndHashCode
    public static class Company {
        private Long idCompany;
        private String companyName;
        private String description;
        private Boolean isActivity;
    }
}
