package pl.pz.oszczedzator3000.exceptions;

public class ExpenseNotFoundException extends RuntimeException{

    public ExpenseNotFoundException(Long expenseId) {
        super("Could not find expense with id = " + expenseId);
    }
}
