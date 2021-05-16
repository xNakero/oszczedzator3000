package pl.pz.oszczedzator3000.exceptions.registration;

public class RegistrationFailedException extends RuntimeException{

    public RegistrationFailedException() {
        super("Something went wrong. Please try again later.");
    }
}
