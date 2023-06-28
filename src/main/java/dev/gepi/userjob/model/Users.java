package dev.gepi.userjob.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    private LocalDate birthday;
    @Column(length = 50)
    private String gender;
    @Column
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

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", familyName='" + familyName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", birthday=" + birthday +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", description='" + description + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                ", isBlocked=" + isBlocked +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Users users = (Users) o;
        return Objects.equals(id, users.id) && Objects.equals(familyName, users.familyName) && Objects.equals(middleName, users.middleName) && Objects.equals(firstName, users.firstName) && Objects.equals(birthday, users.birthday) && Objects.equals(gender, users.gender) && Objects.equals(age, users.age) && Objects.equals(description, users.description) && Objects.equals(created, users.created) && Objects.equals(updated, users.updated) && Objects.equals(isBlocked, users.isBlocked);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, familyName, middleName, firstName, birthday, gender, age, description, created, updated, isBlocked);
    }
}
