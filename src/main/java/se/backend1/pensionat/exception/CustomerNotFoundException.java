package se.backend1.pensionat.exception;

public class CustomerNotFoundException  extends RuntimeException {
    public CustomerNotFoundException(String message) {
        super(message);
    }
}
