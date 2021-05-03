package pl.pz.oszczedzator3000.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @Column(name = "role_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "roles", cascade = CascadeType.ALL)
    private Set<User> users;

}
