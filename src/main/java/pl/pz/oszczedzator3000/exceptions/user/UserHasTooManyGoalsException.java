package pl.pz.oszczedzator3000.exceptions.user;

public class UserHasTooManyGoalsException extends RuntimeException{

    public UserHasTooManyGoalsException() {
        super("Could not add more goals for this user.");
    }
}
