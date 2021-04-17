package pl.pz.oszczedzator3000.exceptions.user;

public class UserHasTooManyGoalsException extends RuntimeException{

    public UserHasTooManyGoalsException(Long userId) {
        super("Could not add more goals for user with id = " + userId);
    }
}
