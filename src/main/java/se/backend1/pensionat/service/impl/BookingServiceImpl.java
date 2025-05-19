package se.backend1.pensionat.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.backend1.pensionat.dto.BookingDto;
import se.backend1.pensionat.dto.DetailedBookingDto;
import se.backend1.pensionat.entity.Booking;
import se.backend1.pensionat.exception.BookingNotFoundException;
import se.backend1.pensionat.exception.CustomerHasBookingsException;
import se.backend1.pensionat.mapper.BookingMapper;
import se.backend1.pensionat.repository.BookingRepository;
import se.backend1.pensionat.service.BookingService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;


    @Override
    public BookingDto createBooking(BookingDto dto) {
        Booking entity = bookingMapper.toEntity(dto);
        Booking saved = bookingRepository.save(entity);
        return bookingMapper.toDto(saved);
    }

    @Override
    public BookingDto updateBooking(Long id, BookingDto dto) {
        Booking existing = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with ID" + id));

        existing.setCheckIn(dto.getCheckIn());
        existing.setCheckOut(dto.getCheckOut());
        existing.setNumberOfGuests(dto.getNumberOfGuests());

        Booking saved= bookingRepository.save(existing);
        return bookingMapper.toDto(saved);
    }

    @Override
    public void deleteBooking(Long id) {
        Booking existing= bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with ID" + id));

        // Om checkOut är idag eller senare → pågående eller framtida bokning
        if (!existing.getCheckOut().isBefore(LocalDate.now())) {
            throw new CustomerHasBookingsException("Customer has bookings, cannot be removed");
        }
        bookingRepository.delete(existing);
    }

    @Override
    public BookingDto getBookingById(Long id) {
        Booking existing = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with ID" + id));
        return bookingMapper.toDto(existing);
    }

    @Override
    public List<BookingDto> getBookingsByCustomer(Long customerId) {
        return bookingRepository.findByCustomerId(customerId).stream()
                .map(bookingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        List<Booking> conflicts = bookingRepository.findConflictingBookings(roomId, checkIn, checkOut);
        return conflicts.isEmpty(); // true = ledigt, false = dubbelbokning
    }


    // Placera denna metod from bookingRepo findConflictingBookings
    @Override
    public DetailedBookingDto getDetailedBooking(Booking booking) {
        return bookingMapper.getDetailedBooking(booking);
    }

    @Override
    public List<BookingDto> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(bookingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> getBookingsForDate(LocalDate date) {
        return bookingRepository.findBookingsByDate(date);
    }

}
