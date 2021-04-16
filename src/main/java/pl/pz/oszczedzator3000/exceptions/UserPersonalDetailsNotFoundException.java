package pl.pz.oszczedzator3000.exceptions;

public class UserPersonalDetailsNotFoundException extends RuntimeException {

    public UserPersonalDetailsNotFoundException(Long userId) {
        super("There is no personal details for a user with an id = " + userId);
    }
}
