package dev.gepi.userjob.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "family_name")
    private String familyName;
    @Column(name = "middle_name")
    private String middleName;
    @Column(name = "first_name")
    private String firstName;
    @Column
    private Date birthday;
    @Column
    private String gender;
    @Column //TODO убрать возраст? (он зависит от даты рождения); возраст в месяцах?
    private Integer age;
    @Column
    private String description;
    @Column
    private java.time.OffsetDateTime created;
    @Column
    private java.time.OffsetDateTime updated;
    @Column(name = "is_blocked")
    private Boolean isBlocked;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserJobInfo> userJobInfoList = new ArrayList<>();

    public void addUserJobInfo(UserJobInfo userJobInfo) {
        userJobInfoList.add(userJobInfo);
        userJobInfo.setUsers(this);
    }

    public void removeUserJobInfo(UserJobInfo userJobInfo) {
        userJobInfoList.remove(userJobInfo);
        userJobInfo.setUsers(null);
    }
}
