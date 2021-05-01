package pl.pz.oszczedzator3000.dto.goalanalyser;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class GoalAnalyserRequestDto {

    @JsonAlias("initial_contribution")
    private double initialContribution;
    @JsonAlias("start_date")
    private LocalDate startDate;
    @JsonAlias("end_date")
    private LocalDate endDate;
}
