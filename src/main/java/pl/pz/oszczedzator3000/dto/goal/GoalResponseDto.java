package pl.pz.oszczedzator3000.dto.goal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.pz.oszczedzator3000.model.enums.Category;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class GoalResponseDto {

    private Long goalId;
    private Category category;
    private String name;
    private double price;
    private LocalDateTime targetDate;
}
