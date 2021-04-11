package pl.pz.oszczedzator3000.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.pz.oszczedzator3000.model.Enum.CategoryEnum;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "goal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Goal {

    @Id
    @Column(name = "goal_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long goalId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private CategoryEnum categoryEnum;

    @Column(nullable = false)
    private String goalName;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private LocalDateTime targetDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
