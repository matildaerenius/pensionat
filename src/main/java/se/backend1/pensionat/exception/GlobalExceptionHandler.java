package se.backend1.pensionat.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            CustomerNotFoundException.class,
            RoomNotFoundException.class,
            BookingNotFoundException.class,
            RoomUnavailableException.class,
            CustomerHasBookingsException.class,
            InvalidBookingDatesException.class
    })

    // TODO : Fixa denna
    public String handleBusinessExceptions() {
        return null;
    }

    // TODO : Fixa denna
    @ExceptionHandler(Exception.class)
    public String handleOtherErrors() {
        return null;
    }
}
