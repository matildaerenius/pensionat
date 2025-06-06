package se.backend1.pensionat.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.backend1.pensionat.dto.BookingDto;
import se.backend1.pensionat.dto.DetailedBookingDto;
import se.backend1.pensionat.entity.Booking;
import se.backend1.pensionat.entity.Customer;
import se.backend1.pensionat.entity.Room;
import se.backend1.pensionat.exception.*;
import se.backend1.pensionat.mapper.BookingMapper;
import se.backend1.pensionat.repository.BookingRepository;
import se.backend1.pensionat.repository.CustomerRepository;
import se.backend1.pensionat.repository.RoomRepository;
import se.backend1.pensionat.service.BookingService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final RoomRepository roomRepository;
    private final BookingMapper bookingMapper;


    @Override
    public BookingDto updateBooking(Long id, BookingDto dto) {
        Booking existing = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with ID " + id));

        boolean roomChanged = !existing.getRoom().getId().equals(dto.getRoomId());
        boolean dateChanged = !existing.getCheckIn().equals(dto.getCheckIn()) || !existing.getCheckOut().equals(dto.getCheckOut());

        if (roomChanged || dateChanged) {
            List<Booking> conflicts = bookingRepository.findConflictingBookings(
                    dto.getRoomId(), dto.getCheckIn(), dto.getCheckOut());

            conflicts.removeIf(b -> b.getId().equals(existing.getId()));

            if (!conflicts.isEmpty()) {
                throw new RoomUnavailableException("Rummet är upptaget under vald period.");
            }
        }

        existing.setCheckIn(dto.getCheckIn());
        existing.setCheckOut(dto.getCheckOut());
        existing.setNumberOfGuests(dto.getNumberOfGuests());

        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException("Kund saknas"));
        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new RoomNotFoundException("Rum saknas"));

        existing.setCustomer(customer);
        existing.setRoom(room);

        return bookingMapper.toDto(bookingRepository.save(existing));
    }

    @Override
    public void deleteBooking(Long id) {
        Booking existing= bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with ID" + id));

        LocalDate today = LocalDate.now();
        // Om checkOut är idag eller senare → pågående eller framtida bokning
        if (!today.isBefore(existing.getCheckIn()) && !today.isAfter(existing.getCheckOut())) {
            throw new ActiveBookingDeletionException("Pågående bokning kan inte raderas.");
        }
        bookingRepository.delete(existing);
    }

    @Override
    public BookingDto getBookingById(Long id) {
        Booking existing = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with ID" + id));
        return bookingMapper.toDto(existing);
    }

    // TODO : Ta bort? används inte
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

    @Override
    public List<DetailedBookingDto> getAllDetailedBookings() {
        return bookingRepository.findAll().stream()
                .map(bookingMapper::getDetailedBooking)
                .collect(Collectors.toList());
    }


    @Override
    public List<BookingDto> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(bookingMapper::toDto)
                .collect(Collectors.toList());
    }


    @Override
    public void save(BookingDto bookingDto) {
        if (!isRoomAvailable(bookingDto.getRoomId(), bookingDto.getCheckIn(), bookingDto.getCheckOut())) {
            throw new RoomUnavailableException("Rummet är inte tillgängligt under vald period.");
        }

        Booking booking = bookingMapper.toEntity(bookingDto);
        Customer customer = customerRepository.findById(bookingDto.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException("Kund saknas"));
        Room room = roomRepository.findById(bookingDto.getRoomId())
                .orElseThrow(() -> new RoomNotFoundException("Rum saknas"));

        booking.setCustomer(customer);
        booking.setRoom(room);
        bookingRepository.save(booking);
    }

    // Ta bort? används inte
    @Override
    public void validateNoDoubleBooking(Room room, LocalDate checkIn, LocalDate checkOut) {
        List<Booking> conflicts = bookingRepository.findConflictingBookings(room.getId(), checkIn, checkOut);
        if (!conflicts.isEmpty()) {
            throw new RoomUnavailableException("Rummet är redan bokat under vald period");
        }
    }
}


