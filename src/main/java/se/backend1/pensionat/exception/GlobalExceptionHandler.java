package se.backend1.pensionat.exception;

import org.springframework.ui.Model;
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

    public String handleBusinessExceptions(RuntimeException ex, Model model) {
        model.addAttribute("errorTitle", "Ett fel har uppstått");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error"; // Om vi har en html sida error.html
    }

    /**
     * Fångar alla andra oväntade fel som inte har specifika @ExceptionHandlers, typ nullpoint
     */
    @ExceptionHandler(Exception.class)
    public String handleOtherErrors(Exception ex, Model model) {
        model.addAttribute("errorTitle", "Oväntat fel");
        model.addAttribute("errorMessage", "Något gick fel. Försök igen senare.");
        return "error"; // Om vi har en html sida error.html, annars kanske vi bara kan logga felet
    }
}
