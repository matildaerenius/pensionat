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
import se.backend1.pensionat.exception.CustomerHasBookingsException;
import se.backend1.pensionat.mapper.BookingMapper;
import se.backend1.pensionat.mapper.CustomerMapper;
import se.backend1.pensionat.repository.BookingRepository;
import se.backend1.pensionat.repository.CustomerRepository;
import se.backend1.pensionat.service.impl.BookingServiceImpl;
import se.backend1.pensionat.service.impl.CustomerServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static jdk.internal.org.objectweb.asm.util.CheckClassAdapter.verify;
import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {


    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingServiceImpl bookingServiceImpl;

    private Booking booking;
    private BookingDto bookingDto;
    private DetailedBookingDto detailedBookingDto;

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
    public void createBookingTest() {
        when(bookingMapper.toEntity(bookingDto)).thenReturn(booking);
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(bookingMapper.toDto(booking)).thenReturn(bookingDto);

        BookingDto result = bookingServiceImpl.createBooking(bookingDto);

        assertEquals(bookingDto.getId(), result.getId());
        assertEquals(bookingDto.getCheckIn(), result.getCheckIn());
        assertEquals(bookingDto.getCheckOut(), result.getCheckOut());
    }

    @Test
    public void updateBookingTest() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(bookingMapper.toDto(booking)).thenReturn(bookingDto);

        BookingDto updated = bookingServiceImpl.updateBooking(1L, bookingDto);

        assertEquals(bookingDto.getCheckIn(), updated.getCheckIn());
        assertEquals(bookingDto.getCheckOut(), updated.getCheckOut());
        assertEquals(bookingDto.getNumberOfGuests(), updated.getNumberOfGuests());
    }

    @Test
    public void deleteBookingTest() {
        booking.setCheckOut(LocalDate.of(2023, 6, 1)); // tidigare än idag
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        bookingServiceImpl.deleteBooking(1L);

        verify(bookingRepository).delete(booking);
    }

    @Test
    public void deleteBookingTest_WithError() {
        booking.setCheckOut(LocalDate.now().plusDays(1)); // framtida
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        assertThrows(CustomerHasBookingsException.class, () -> {
            bookingServiceImpl.deleteBooking(1L);
        });
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
    public void getBookingsForDateTest() {
        LocalDate date= LocalDate.now();
        when(bookingRepository.findBookingsByDate((date)))
                .thenReturn(List.of(booking));

        List<Booking> result = bookingServiceImpl.getBookingsForDate(LocalDate.now());

        assertEquals(1, result.size());
        assertEquals(booking.getId(), result.get(0).getId());
    }

    @Test
    public void saveTest() {
        when(bookingMapper.toEntity(bookingDto)).thenReturn(booking);

        bookingServiceImpl.save(bookingDto);

        verify(bookingRepository).save(booking);
    }


}
