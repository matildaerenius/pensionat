package se.backend1.pensionat.exception;

public class RoomHasBookingsException extends RuntimeException {
  public RoomHasBookingsException(String message) {
    super(message);
  }
}
