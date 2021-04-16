package pl.pz.oszczedzator3000.exceptions.expense;

public class ExpenseNotFoundException extends RuntimeException{

    public ExpenseNotFoundException(Long expenseId) {
        super("Could not find expense with id = " + expenseId);
    }
}
