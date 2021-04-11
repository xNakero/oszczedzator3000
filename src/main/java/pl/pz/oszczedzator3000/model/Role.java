package pl.pz.oszczedzator3000.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.pz.oszczedzator3000.model.Enum.RoleEnum;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    private static final long serialVersionUID = 1L;

    private static final String PREFIX = "ROLE_";

    @Id
    @Column(name = "role_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long roleId;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "authorities")
    private Set<User> users;

    public Role(RoleEnum name) {
        this.name = PREFIX + name.toString();
    }

    @Override
    public String toString() {
        return name;
    }

}
