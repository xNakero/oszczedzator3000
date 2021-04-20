package pl.pz.oszczedzator3000.dto.goalanalyser;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GoalAnalyserResponseDto {

    private boolean canBeAchieved;
    private double moneyToCollect;
    private double averagePerDayPossibleSavings;
    private double averagePerDayNecessarySavings;
    private boolean canBeAchievedUntilEndDate;
}
