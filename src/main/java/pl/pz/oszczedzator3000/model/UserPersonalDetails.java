package pl.pz.oszczedzator3000.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.pz.oszczedzator3000.model.enums.Profession;
import pl.pz.oszczedzator3000.model.enums.RelationshipStatus;
import pl.pz.oszczedzator3000.model.enums.Sex;

import javax.persistence.*;

@Entity
@Table(name = "user_personal_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPersonalDetails {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false)
    private double salary;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Profession profession;

    @Column(nullable = false)
    private int age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Sex sex;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RelationshipStatus relationshipStatus;

    @Column(nullable = false)
    private int kids;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
