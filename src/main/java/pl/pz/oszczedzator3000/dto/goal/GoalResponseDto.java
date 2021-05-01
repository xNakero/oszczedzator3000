package pl.pz.oszczedzator3000.dto.goal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.pz.oszczedzator3000.model.enums.Category;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class GoalResponseDto {

    @JsonProperty("goal_id")
    private Long goalId;
    private Category category;
    private String name;
    private double price;
    @JsonProperty("target_date")
    private LocalDate targetDate;
}
