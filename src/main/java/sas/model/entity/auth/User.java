package sas.model.entity.auth;

import sas.model.entity.auth._enum.EOperationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(unique = true)
    private String username;
    @Column
    private String password;
    @Column
    private String salt;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private String emailAddress;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_name", referencedColumnName = "name", unique = true)
    private Profile profile;
    @Column(name = "profile_name", insertable=false, updatable=false)
    private String profileName;
    @Column
    private Boolean isFirstLogin;

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean hasRight(Operation operation) {
        return operation.getOperation() == EOperationType.NONE ||
                                profile.hasRight(operation) && !isFirstLogin;
    }
}
