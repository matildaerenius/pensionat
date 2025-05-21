package se.backend1.pensionat.exception;

public class ActiveBookingDeletionException extends RuntimeException {
    public ActiveBookingDeletionException(String message) {
        super(message);
    }
}
