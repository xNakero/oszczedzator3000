package pl.pz.oszczedzator3000.dto.goal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.pz.oszczedzator3000.model.enums.Category;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class GoalRequestDetailsDto extends GoalRequestDto {

    private int toTargetDate;
    private double amount;
    private boolean isPossible;
}
