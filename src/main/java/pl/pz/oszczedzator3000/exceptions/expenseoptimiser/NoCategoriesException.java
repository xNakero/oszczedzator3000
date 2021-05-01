package pl.pz.oszczedzator3000.exceptions.expenseoptimiser;

public class NoCategoriesException extends RuntimeException{

    public NoCategoriesException() {
        super("No category in the request body");
    }
}
