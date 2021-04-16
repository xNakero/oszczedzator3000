package pl.pz.oszczedzator3000.mapper;

import org.mapstruct.*;
import org.springframework.stereotype.Repository;
import pl.pz.oszczedzator3000.dto.expense.ExpenseRequestDto;
import pl.pz.oszczedzator3000.dto.expense.ExpenseResponseDto;
import pl.pz.oszczedzator3000.model.Expense;

@Mapper(componentModel = "spring")
@Repository
public interface ExpenseMapper {

    Expense mapToExpense(ExpenseRequestDto expenseRequestDto);

    ExpenseResponseDto mapToExpenseResponseDto(Expense expense);

}
