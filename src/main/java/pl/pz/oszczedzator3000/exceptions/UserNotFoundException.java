package pl.pz.oszczedzator3000.exceptions;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(Long userId) {
        super("Could not find user with id = " + userId);
    }
}
