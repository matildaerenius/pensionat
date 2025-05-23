package se.backend1.pensionat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetailedCustomerDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<BookingDto> bookings;

}
