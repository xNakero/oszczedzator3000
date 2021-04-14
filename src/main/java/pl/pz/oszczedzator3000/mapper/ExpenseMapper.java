package pl.pz.oszczedzator3000.mapper;

import org.aspectj.lang.annotation.After;
import org.mapstruct.*;
import org.springframework.stereotype.Repository;
import pl.pz.oszczedzator3000.dto.ExpenseRequestDto;
import pl.pz.oszczedzator3000.dto.ExpenseResponseDto;
import pl.pz.oszczedzator3000.model.Expense;
import pl.pz.oszczedzator3000.model.enums.Category;

import java.util.Locale;

@Mapper(componentModel = "spring")
@Repository
public interface ExpenseMapper {

    Expense mapToExpense(ExpenseRequestDto expenseRequestDto);

    ExpenseResponseDto mapToExpenseResponseDto(Expense expense);

}
