package pl.pz.oszczedzator3000.model;

import lombok.*;
import pl.pz.oszczedzator3000.model.enums.Category;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "expense")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Expense {

    @Id
    @Column(name = "expense_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private Category category;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double value;

    @Column(nullable = false)
    private LocalDate date;

    @ToString.Exclude
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH, CascadeType.PERSIST})
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
