package se.backend1.pensionat.mapper;

import se.backend1.pensionat.dto.BookingDto;
import se.backend1.pensionat.entity.Booking;

public class BookingMapper {

    public static Booking toEntity(BookingDto dto) {
        if (dto == null) return null;
        Booking booking = new Booking();
        booking.setId(dto.getId());
        booking.setCheckIn(dto.getCheckIn());
        booking.setCheckOut(dto.getCheckOut());
        booking.setNumberOfGuests(dto.getNumberOfGuests());
        // customer och room sätts i service?
        return booking;
    }

    public static BookingDto toDto(Booking booking) {
        if (booking == null) return null;
        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setCheckIn(booking.getCheckIn());
        dto.setCheckOut(booking.getCheckOut());
        dto.setNumberOfGuests(booking.getNumberOfGuests());
        dto.setCustomerId(booking.getCustomer().getId());
        dto.setRoomId(booking.getRoom().getId());
        return dto;
    }
}
