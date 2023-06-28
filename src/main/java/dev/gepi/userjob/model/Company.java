package dev.gepi.userjob.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "company_name")
    private String companyName;
    @Column(name = "description")
    private String description;
    @Column
    private java.time.OffsetDateTime created;
    @Column
    private java.time.OffsetDateTime updated;
    @Column(name = "is_activity")
    private Boolean isActivity;
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserJobInfo> userJobInfoList = new ArrayList<>();

    public void addUserJobInfo(UserJobInfo userJobInfo) {
        userJobInfoList.add(userJobInfo);
        userJobInfo.setCompany(this);
    }

    public void removeUserJobInfo(UserJobInfo userJobInfo) {
        userJobInfoList.remove(userJobInfo);
        userJobInfo.setCompany(null);
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", companyName='" + companyName + '\'' +
                ", description='" + description + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                ", isActivity=" + isActivity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return Objects.equals(id, company.id) && Objects.equals(companyName, company.companyName) && Objects.equals(description, company.description) && Objects.equals(created, company.created) && Objects.equals(updated, company.updated) && Objects.equals(isActivity, company.isActivity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, companyName, description, created, updated, isActivity);
    }
}
