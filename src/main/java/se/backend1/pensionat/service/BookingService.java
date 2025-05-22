package se.backend1.pensionat.service;

import jakarta.validation.Valid;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.backend1.pensionat.dto.BookingDto;
import se.backend1.pensionat.dto.DetailedBookingDto;
import se.backend1.pensionat.dto.RoomDto;
import se.backend1.pensionat.entity.Booking;
import se.backend1.pensionat.entity.Room;
import se.backend1.pensionat.repository.BookingRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingService  {
    //KLAR
    BookingDto createBooking(BookingDto dto);
    // KLAR
    BookingDto updateBooking(Long id, BookingDto dto);
    //Dubbelkolla!!!!
    void deleteBooking(Long id);
    //KLAR
    BookingDto getBookingById(Long id);
    //KLAR
    List<BookingDto> getBookingsByCustomer(Long customerId);
    //KLAR
    boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut);
    //KLAR
    DetailedBookingDto getDetailedBooking(Booking booking);
    //KLAR
    List<BookingDto> getAllBookings();

    //Gammalt eller oklart om detta ska med
    List<Booking> getBookingsForDate(LocalDate date);

    //senaste trellometod
    void checkConflictingAndSave(BookingDto dto);

    void save(@Valid BookingDto bookingDto);
}

