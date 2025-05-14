package se.backend1.pensionat.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {

    private Long id;

    @NotNull(message = "Incheckningsdatum krävs")
    private LocalDate checkIn;

    @NotNull(message = "Utcheckningsdatum krävs")
    private LocalDate checkOut;

    @Min(value = 1, message = "Minst en gäst krävs")
    private int numberOfGuests;

    @NotNull(message = "Kund måste väljas")
    private Long customerId;

    @NotNull(message = "Rum måste väljas")
    private Long roomId;
}
