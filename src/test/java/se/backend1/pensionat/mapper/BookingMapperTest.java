package se.backend1.pensionat.mapper;

import org.junit.jupiter.api.Test;
import se.backend1.pensionat.dto.BookingDto;
import se.backend1.pensionat.entity.Booking;
import se.backend1.pensionat.entity.Customer;
import se.backend1.pensionat.entity.Room;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class BookingMapperTest {

    @Test
    void shouldMapDtoToEntityCorrectly() {
        BookingDto dto = new BookingDto();
        dto.setId(1L);
        dto.setCheckIn(LocalDate.of(2025, 6, 1));
        dto.setCheckOut(LocalDate.of(2025, 6, 5));
        dto.setNumberOfGuests(2);
        dto.setCustomerId(10L);
        dto.setRoomId(20L);

        Booking entity = BookingMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getCheckIn(), entity.getCheckIn());
        assertEquals(dto.getCheckOut(), entity.getCheckOut());
        assertEquals(dto.getNumberOfGuests(), entity.getNumberOfGuests());

        assertNull(entity.getCustomer());
        assertNull(entity.getRoom());
    }

    @Test
    void shouldMapEntityToDtoCorrectly() {
        Customer customer = new Customer();
        customer.setId(10L);

        Room room = new Room();
        room.setId(20L);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setCheckIn(LocalDate.of(2025, 6, 1));
        booking.setCheckOut(LocalDate.of(2025, 6, 5));
        booking.setNumberOfGuests(2);
        booking.setCustomer(customer);
        booking.setRoom(room);

        BookingDto dto = BookingMapper.toDto(booking);

        assertNotNull(dto);
        assertEquals(booking.getId(), dto.getId());
        assertEquals(booking.getCheckIn(), dto.getCheckIn());
        assertEquals(booking.getCheckOut(), dto.getCheckOut());
        assertEquals(booking.getNumberOfGuests(), dto.getNumberOfGuests());
        assertEquals(booking.getCustomer().getId(), dto.getCustomerId());
        assertEquals(booking.getRoom().getId(), dto.getRoomId());
    }

    @Test
    void shouldReturnNullIfInputIsNull() {
        assertNull(BookingMapper.toDto(null));
        assertNull(BookingMapper.toEntity(null));
    }
}


