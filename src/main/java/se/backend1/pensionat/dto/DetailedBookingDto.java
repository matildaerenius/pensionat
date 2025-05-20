package se.backend1.pensionat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.backend1.pensionat.model.RoomType;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetailedBookingDto {

    // Kundinformation
    private Long customerId;
    private String firstName;
    private String lastName;

    // Rumsinformation
    private Long roomId;
    private String roomNumber;
    private RoomType roomType;
    private int numberOfGuests;

    private BookingDto bookingDto;
}
