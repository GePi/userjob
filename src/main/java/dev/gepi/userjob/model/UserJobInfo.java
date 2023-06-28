package dev.gepi.userjob.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_job_info", uniqueConstraints = @UniqueConstraint(columnNames = {"id_company", "user_id"}))
public class UserJobInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_company")
    private Company company;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;
    @Column(name = "description")
    private String description;
    @Column
    private java.time.OffsetDateTime created;
    @Column
    private java.time.OffsetDateTime updated;
    @Column(name = "is_activity")
    private Boolean isActivity;

    @Override
    public String toString() {
        return "UserJobInfo{" +
                "id=" + id +
                ", users=" + users +
                ", description='" + description + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                ", isActivity=" + isActivity +
                '}';
    }
}
