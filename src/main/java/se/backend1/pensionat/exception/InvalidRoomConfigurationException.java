package se.backend1.pensionat.exception;

public class InvalidRoomConfigurationException extends RuntimeException {
  public InvalidRoomConfigurationException(String message) {
    super(message);
  }
}
