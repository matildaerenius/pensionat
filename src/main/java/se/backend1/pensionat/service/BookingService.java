package se.backend1.pensionat.service;

import jakarta.validation.Valid;
import se.backend1.pensionat.dto.BookingDto;
import se.backend1.pensionat.dto.DetailedBookingDto;
import se.backend1.pensionat.entity.Booking;
import se.backend1.pensionat.entity.Room;

import java.time.LocalDate;
import java.util.List;

public interface BookingService  {

    BookingDto updateBooking(Long id, BookingDto dto);

    void deleteBooking(Long id);

    BookingDto getBookingById(Long id);

    List<BookingDto> getBookingsByCustomer(Long customerId);

    boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut);

    List<DetailedBookingDto> getAllDetailedBookings();

    List<BookingDto> getAllBookings();

    void save(@Valid BookingDto bookingDto);

    void validateNoDoubleBooking(Room room, LocalDate checkIn, LocalDate checkOut);
}

