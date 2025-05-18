package se.backend1.pensionat.service;

import se.backend1.pensionat.dto.BookingDto;
import se.backend1.pensionat.dto.DetailedBookingDto;
import se.backend1.pensionat.entity.Booking;
import se.backend1.pensionat.repository.BookingRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingService  {
    //Nytt
    BookingDto createBooking(BookingDto dto);
    void saveBooking(Booking booking); //OKLART OM DETTA SKA MED
    BookingDto updateBooking(Long id, BookingDto dto);
    void cancelBooking(Long id);
    BookingDto getBookingById(Long id);
    List<BookingDto> getBookingsByCustomer(Long customerId);
    boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut);



    //Gammalt oklart om detta ska med
    DetailedBookingDto getDetailedBooking(Booking booking);
    List<Booking> getBookingsForDate(LocalDate date);
    List<Booking> getAllBookings();
}
