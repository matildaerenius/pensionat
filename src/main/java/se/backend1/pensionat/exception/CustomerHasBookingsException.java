package se.backend1.pensionat.exception;

public class CustomerHasBookingsException extends RuntimeException {
    public CustomerHasBookingsException(String message) {
        super(message);
    }
}
