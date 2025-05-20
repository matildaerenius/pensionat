package se.backend1.pensionat.mapper;

import org.junit.jupiter.api.Test;
import se.backend1.pensionat.dto.BookingDto;
import se.backend1.pensionat.entity.Booking;
import se.backend1.pensionat.entity.Customer;
import se.backend1.pensionat.entity.Room;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class BookingMapperTest {

    private final BookingMapper bookingMapper = new BookingMapper(null, null); // mapper-dependencys används ej här

    @Test
    public void testToDto() {
        Customer customer = Customer.builder()
                .id(1L)
                .firstName("Test")
                .lastName("Testsson")
                .build();

        Room room = Room.builder()
                .id(2L)
                .roomNumber("101")
                .build();

        Booking booking = Booking.builder()
                .id(1L)
                .checkIn(LocalDate.of(2025, 5, 1))
                .checkOut(LocalDate.of(2025, 5, 5))
                .numberOfGuests(2)
                .customer(customer)
                .room(room)
                .build();

        BookingDto dto = bookingMapper.toDto(booking);

        assertEquals(booking.getId(), dto.getId());
        assertEquals(booking.getCheckIn(), dto.getCheckIn());
        assertEquals(booking.getCheckOut(), dto.getCheckOut());
        assertEquals(booking.getNumberOfGuests(), dto.getNumberOfGuests());
        assertEquals(booking.getCustomer().getId(), dto.getCustomerId());
        assertEquals(booking.getRoom().getId(), dto.getRoomId());
    }
    @Test
    public void testToEntity() {
        BookingDto dto = BookingDto.builder()
                .id(2L)
                .checkIn(LocalDate.of(2025, 6, 1))
                .checkOut(LocalDate.of(2025, 6, 10))
                .numberOfGuests(3)
                .build();

        Booking booking = bookingMapper.toEntity(dto);

        assertEquals(dto.getId(), booking.getId());
        assertEquals(dto.getCheckIn(), booking.getCheckIn());
        assertEquals(dto.getCheckOut(), booking.getCheckOut());
        assertEquals(dto.getNumberOfGuests(), booking.getNumberOfGuests());
    }
}

