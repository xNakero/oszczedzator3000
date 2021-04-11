package pl.pz.oszczedzator3000.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long goalId;

    @Enumerated(EnumType.STRING)
    @Column(name = "category_enum", nullable = false)
    private CategoryEnum categoryEnum;

    @Column(name = "goal_name", nullable = false)
    private String goalName;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "target_date", nullable = false)
    private LocalDateTime targetDate;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
