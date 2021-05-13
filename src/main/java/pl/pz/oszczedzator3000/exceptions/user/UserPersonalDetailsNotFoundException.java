package pl.pz.oszczedzator3000.exceptions.user;

public class UserPersonalDetailsNotFoundException extends RuntimeException {

    public UserPersonalDetailsNotFoundException() {
        super("There is no personal details for this user");
    }
}
