package pl.pz.oszczedzator3000.dto.goal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.pz.oszczedzator3000.model.enums.Category;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class GoalFilterRequestDto {

    private Category category;
}
