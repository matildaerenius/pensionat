package se.backend1.pensionat.service;

import se.backend1.pensionat.entity.Booking;
import se.backend1.pensionat.repository.BookingRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingService  {
    List<Booking> getBookingsForDate(LocalDate date);

    boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut);

    void saveBooking(Booking booking);

    List<Booking> getAllBookings();

}
