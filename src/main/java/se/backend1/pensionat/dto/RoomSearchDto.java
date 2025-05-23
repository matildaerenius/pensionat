package se.backend1.pensionat.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomSearchDto {

    @NotNull(message = "Incheckningsdatum krävs")
    private LocalDate checkIn;

    @NotNull(message = "Utcheckningsdatum krävs")
    private LocalDate checkOut;

    @NotNull(message = "Antal gäster krävs")
    @Min(value = 1, message = "Minst en gäst krävs")
    private Integer numberOfGuests;
}
