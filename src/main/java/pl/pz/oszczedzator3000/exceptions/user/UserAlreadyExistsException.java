package pl.pz.oszczedzator3000.exceptions.user;

public class UserAlreadyExistsException extends RuntimeException{

    public UserAlreadyExistsException(String username) {
        super("User " + username + " already exists.");
    }
}
