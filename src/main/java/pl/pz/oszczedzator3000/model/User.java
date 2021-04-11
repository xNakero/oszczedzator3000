package pl.pz.oszczedzator3000.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "user_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true, length = 20)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @ManyToMany(targetEntity = Role.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
//    @JoinTable(name = "user_role",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> authorities;

    @JsonManagedReference
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Goal> goals;

    @JsonManagedReference
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Expense> expenses;

    @Column(nullable = false, updatable = false)
    private LocalDateTime creationDate;

    public User(String username, String password, String email, Set<Role> authorities, Set<Goal> goals, Set<Expense> expenses, LocalDateTime creationDate) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.authorities = authorities;
        this.goals = goals;
        this.expenses = expenses;
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getUserId().equals(user.getUserId()) && getUsername().equals(user.getUsername()) && getPassword().equals(user.getPassword()) && getEmail().equals(user.getEmail()) && Objects.equals(getAuthorities(), user.getAuthorities()) && Objects.equals(getGoals(), user.getGoals()) && Objects.equals(getExpenses(), user.getExpenses()) && getCreationDate().equals(user.getCreationDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getUsername(), getPassword(), getEmail(), getAuthorities(), getGoals(), getExpenses(), getCreationDate());
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", authorities=" + authorities +
                ", goals=" + goals +
                ", expenses=" + expenses +
                ", creationDate=" + creationDate +
                '}';
    }
}
