package pl.pz.oszczedzator3000.exceptions.user;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(Long userId) {
        super("Could not find user with id = " + userId);
    }

    public UserNotFoundException() {
        super("Could not find user");
    }
}
