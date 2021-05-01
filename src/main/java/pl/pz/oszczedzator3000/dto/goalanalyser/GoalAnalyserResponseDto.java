package pl.pz.oszczedzator3000.dto.goalanalyser;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GoalAnalyserResponseDto {

    @JsonProperty("can_achieve")
    private boolean canAchieve;
    @JsonProperty("money_to_collect")
    private double moneyToCollect;
    @JsonProperty("average_daily_possible_savings")
    private double averageDailyPossibleSavings;
    @JsonProperty("average_daily_necessary_savings")
    private double averageDailyNecessarySavings;
    @JsonProperty("can_achieve_before_end_date")
    private boolean canAchieveBeforeEndDate;
}
