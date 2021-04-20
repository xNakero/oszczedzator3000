package pl.pz.oszczedzator3000.dto.goalanalyser;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class GoalAnalyserRequestDto {

    private double initialContribution;
    private LocalDate startDate;
    private LocalDate endDate;
}
