package se.backend1.pensionat.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.backend1.pensionat.dto.BookingDto;
import se.backend1.pensionat.dto.DetailedBookingDto;
import se.backend1.pensionat.entity.Booking;
import se.backend1.pensionat.entity.Customer;
import se.backend1.pensionat.repository.BookingRepository;
import se.backend1.pensionat.repository.CustomerRepository;
import se.backend1.pensionat.service.BookingService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void deleteBookingById(Long id) {

        bookingRepository.deleteById(id);
    }


    @Override
    public List<Booking> getBookingsForDate(LocalDate date) {
        return bookingRepository.findBookingsByDate(date);
    }
    @Override

    public boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        List<Booking> conflicts = bookingRepository.findConflictingBookings(roomId, checkIn, checkOut);
        return conflicts.isEmpty(); // true = ledigt, false = dubbelbokning
    }

    @Override
    public void saveBooking(Booking booking) {

    }


    @Override
    public List<Booking> getAllBookings() {
        return List.of();
    }

}
