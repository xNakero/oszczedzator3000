package pl.pz.oszczedzator3000.dto.expenseoptimiser;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class ExpenseOptimiserResponseDto {

    @JsonProperty("user_data")
    private Set<ExpenseOptimiserResponseSingleDataDto> userData;
    @JsonProperty("similar_users_data")
    private Set<ExpenseOptimiserResponseSingleDataDto> similarUsersData;
}
