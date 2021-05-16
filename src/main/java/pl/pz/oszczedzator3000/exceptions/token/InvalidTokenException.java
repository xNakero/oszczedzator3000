package pl.pz.oszczedzator3000.exceptions.token;

public class InvalidTokenException extends RuntimeException{

    public InvalidTokenException() {
        super("No such token was found");
    }
}
