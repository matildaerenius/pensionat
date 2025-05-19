package se.backend1.pensionat.service;

import jakarta.validation.Valid;
import se.backend1.pensionat.dto.BookingDto;
import se.backend1.pensionat.dto.DetailedBookingDto;
import se.backend1.pensionat.entity.Booking;
import se.backend1.pensionat.entity.Room;
import se.backend1.pensionat.repository.BookingRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingService  {

  //  DetailedBookingDto getDetailedBooking(Booking booking);

    //BookingDto bookingToBookingDto(Booking booking);


    List<Booking> getBookingsForDate(LocalDate date);

    boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut);

    void saveBooking(Booking booking);


    List<Booking> getAllBookings();

    List<Room> findAvailableRooms(String checkIn, String checkOut, int guests);

    List<Booking> getBookingsByRoomId(Long roomId);

    void deleteBooking(Long id);

    void updateBooking(Long id, @Valid BookingDto bookingDto);

    Booking getBookingById(Long id);
}
