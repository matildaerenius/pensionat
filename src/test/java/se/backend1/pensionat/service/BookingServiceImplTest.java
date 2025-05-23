package se.backend1.pensionat.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import se.backend1.pensionat.dto.BookingDto;
import se.backend1.pensionat.dto.DetailedBookingDto;
import se.backend1.pensionat.entity.Booking;
import se.backend1.pensionat.entity.Customer;
import se.backend1.pensionat.entity.Room;
import se.backend1.pensionat.exception.ActiveBookingDeletionException;
import se.backend1.pensionat.exception.CustomerHasBookingsException;
import se.backend1.pensionat.exception.RoomUnavailableException;
import se.backend1.pensionat.mapper.BookingMapper;
import se.backend1.pensionat.repository.BookingRepository;
import se.backend1.pensionat.repository.CustomerRepository;
import se.backend1.pensionat.repository.RoomRepository;
import se.backend1.pensionat.service.impl.BookingServiceImpl;


import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {


    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private RoomRepository roomRepository;


    @InjectMocks
    private BookingServiceImpl bookingServiceImpl;

    private Booking booking;
    private BookingDto bookingDto;

    @BeforeEach
    public void setUp() {
        booking = new Booking();
        booking.setId(1L);
        booking.setCheckIn(LocalDate.of(2025, 6, 1));
        booking.setCheckOut(LocalDate.of(2025, 6, 5));
        booking.setNumberOfGuests(2);

        bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setCheckIn(booking.getCheckIn());
        bookingDto.setCheckOut(booking.getCheckOut());
        bookingDto.setNumberOfGuests(2);

    }

    @Test
    public void updateBookingTest() {

        bookingDto.setCustomerId(1L);
        bookingDto.setRoomId(1L);
        bookingDto.setCheckIn(LocalDate.of(2025, 6, 1));
        bookingDto.setCheckOut(LocalDate.of(2025, 6, 5));

        Room existingRoom = new Room();
        existingRoom.setId(1L);
        booking.setRoom(existingRoom);
        booking.setCheckIn(LocalDate.of(2025, 6, 1));
        booking.setCheckOut(LocalDate.of(2025, 6, 5));

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(new Customer()));
        when(roomRepository.findById(1L)).thenReturn(Optional.of(existingRoom));

        lenient().when(bookingRepository.findConflictingBookings(1L, bookingDto.getCheckIn(), bookingDto.getCheckOut()))
                .thenReturn(Collections.emptyList());

        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(bookingMapper.toDto(any(Booking.class))).thenReturn(bookingDto);

        BookingDto result = bookingServiceImpl.updateBooking(1L, bookingDto);

        assertEquals(bookingDto.getCheckIn(), result.getCheckIn());
        assertEquals(bookingDto.getCheckOut(), result.getCheckOut());
        assertEquals(bookingDto.getNumberOfGuests(), result.getNumberOfGuests());
    }

    @Test
    public void deleteBookingTest() {
        booking.setCheckIn(LocalDate.of(2023, 5, 1));
        booking.setCheckOut(LocalDate.of(2023, 6, 1)); // Avslutad

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        bookingServiceImpl.deleteBooking(1L);

        verify(bookingRepository).delete(booking);
    }

    @Test
    public void deleteBookingTest_WithError() {
        booking.setCheckIn(LocalDate.now().minusDays(1));
        booking.setCheckOut(LocalDate.now().plusDays(1));
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        assertThrows(ActiveBookingDeletionException.class, () -> {
            bookingServiceImpl.deleteBooking(1L);
        });

        verify(bookingRepository, never()).delete(any());
    }


    @Test
    public void getBookingByIdTest() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingMapper.toDto(booking)).thenReturn(bookingDto);

        BookingDto result = bookingServiceImpl.getBookingById(1L);

        assertEquals(bookingDto.getId(), result.getId());
    }

    @Test
    public void getBookingsByCustomerTest() {
        List<Booking> bookings = List.of(booking);
        when(bookingRepository.findByCustomerId(1L)).thenReturn(bookings);
        when(bookingMapper.toDto(booking)).thenReturn(bookingDto);

        List<BookingDto> result = bookingServiceImpl.getBookingsByCustomer(1L);

        assertEquals(1, result.size());
        assertEquals(bookingDto.getId(), result.get(0).getId());
    }

    @Test
    public void isRoomAvailableTest_True() {
        when(bookingRepository.findConflictingBookings(1L,
                booking.getCheckIn(), booking.getCheckOut())).thenReturn(List.of());

        boolean result = bookingServiceImpl.isRoomAvailable(1L,
                booking.getCheckIn(), booking.getCheckOut());

        assertTrue(result);
        assertFalse(!result);
    }

    @Test
    public void isRoomAvailableTest_False() {
        when(bookingRepository.findConflictingBookings(1L,
                booking.getCheckIn(), booking.getCheckOut())).thenReturn(List.of(booking));

        boolean result = bookingServiceImpl.isRoomAvailable(1L,
                booking.getCheckIn(), booking.getCheckOut());

        assertFalse(result);
        assertTrue(!result);
    }

    @Test
    public void getAllBookingsTest() {
        List<Booking> bookings = List.of(booking);
        when(bookingRepository.findAll()).thenReturn(bookings);
        when(bookingMapper.toDto(booking)).thenReturn(bookingDto);

        List<BookingDto> result = bookingServiceImpl.getAllBookings();

        assertEquals(1, result.size());
        assertEquals(bookingDto.getId(), result.get(0).getId());
    }

    @Test
    public void saveTest() {
        bookingDto.setCustomerId(1L);
        bookingDto.setRoomId(1L);

        Customer customer = new Customer();
        customer.setId(1L);

        Room room = new Room();
        room.setId(1L);

        when(bookingMapper.toEntity(bookingDto)).thenReturn(booking);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(bookingRepository.findConflictingBookings(1L, bookingDto.getCheckIn(), bookingDto.getCheckOut()))
                .thenReturn(Collections.emptyList());

        bookingServiceImpl.save(bookingDto);

        verify(bookingRepository).save(booking);
    }



}
