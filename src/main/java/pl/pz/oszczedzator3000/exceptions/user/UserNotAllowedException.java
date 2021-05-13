package pl.pz.oszczedzator3000.exceptions.user;

public class UserNotAllowedException extends RuntimeException{

    public UserNotAllowedException() {
        super("User has no rights for this area");
    }
}
