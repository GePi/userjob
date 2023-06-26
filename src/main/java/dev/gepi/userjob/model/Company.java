package dev.gepi.userjob.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
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
}
