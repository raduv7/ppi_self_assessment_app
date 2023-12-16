package sas.model.entity.auth;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "profiles")
// index on name due to being unique
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    protected Long id;
    @Column(unique = true)
    private String name;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Operation> rights;

    public boolean hasRight(Operation operation) {
        return rights.stream().anyMatch(right -> right.equals(operation));
    }
}
