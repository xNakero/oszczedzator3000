package pl.pz.oszczedzator3000.exceptions.token;

public class InvalidTokenException extends RuntimeException{

    public InvalidTokenException(String message) {
        super(message);
    }
}
