package pl.pz.oszczedzator3000.exceptions.user;

public class UserNotAllowedException extends RuntimeException{

    public UserNotAllowedException(Long userId) {
        super("User with id = " + userId + " has no rights for this area!");
    }
}
