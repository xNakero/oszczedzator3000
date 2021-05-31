package pl.pz.oszczedzator3000.exceptions.registration;

public class InvalidRegistrationDataException extends RuntimeException{

    public InvalidRegistrationDataException() {
        super("Invalid registration data.");
    }
}
