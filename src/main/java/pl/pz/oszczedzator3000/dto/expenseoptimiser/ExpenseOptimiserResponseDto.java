package pl.pz.oszczedzator3000.dto.expenseoptimiser;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class ExpenseOptimiserResponseDto {

    private Set<ExpenseOptimiserResponseSingleDataDto> userData;
    private Set<ExpenseOptimiserResponseSingleDataDto> allData;
    private Set<ExpenseOptimiserResponseSingleDataDto> similarUsersData;
}
