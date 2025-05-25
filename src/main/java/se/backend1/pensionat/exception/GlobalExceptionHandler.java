package se.backend1.pensionat.exception;

import org.springframework.ui.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Fångar alla förväntade (egna) fel
     */
    @ExceptionHandler({
            CustomerNotFoundException.class,
            RoomNotFoundException.class,
            BookingNotFoundException.class,
            RoomUnavailableException.class,
            CustomerHasBookingsException.class,
            ActiveBookingDeletionException.class
    })

    public String handleBusinessExceptions(RuntimeException ex, Model model) {
        logger.warn("Affärsfel uppstod: {}", ex.getMessage());

        model.addAttribute("errorTitle", "Ett fel har uppstått");
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("timestamp", LocalDateTime.now());
        return "error"; // Om vi har en html sida error.html
    }

    /**
     * Fångar alla andra oväntade fel som inte har specifika @ExceptionHandlers, typ nullpoint
     */
    @ExceptionHandler(Exception.class)
    public String handleOtherErrors(Exception ex, Model model) {
        logger.error("Oväntat fel inträffade:", ex);

        model.addAttribute("errorTitle", "Oväntat fel");
        model.addAttribute("errorMessage", "Något gick fel. Försök igen senare.");
        model.addAttribute("timestamp", LocalDateTime.now());
        return "error"; // Om vi har en html sida error.html, annars kanske vi bara kan logga felet
    }
}
