package se.backend1.pensionat.exception;

public class RoomNumberAlreadyExistsException extends RuntimeException {
    public RoomNumberAlreadyExistsException(String message) {
        super(message);
    }
}
