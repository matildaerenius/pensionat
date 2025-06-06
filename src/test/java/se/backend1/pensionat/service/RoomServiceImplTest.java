package se.backend1.pensionat.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import se.backend1.pensionat.dto.RoomDto;
import se.backend1.pensionat.entity.Booking;
import se.backend1.pensionat.entity.Customer;
import se.backend1.pensionat.entity.Room;
import se.backend1.pensionat.exception.RoomNotFoundException;
import se.backend1.pensionat.mapper.BookingMapper;
import se.backend1.pensionat.mapper.CustomerMapper;
import se.backend1.pensionat.mapper.RoomMapper;
import se.backend1.pensionat.model.RoomType;
import se.backend1.pensionat.repository.BookingRepository;
import se.backend1.pensionat.repository.CustomerRepository;
import se.backend1.pensionat.repository.RoomRepository;
import se.backend1.pensionat.service.impl.CustomerServiceImpl;
import se.backend1.pensionat.service.impl.RoomServiceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class RoomServiceImplTest {

    @Mock
    private RoomMapper roomMapper;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private BookingRepository bookingRepository;


    @InjectMocks
    private RoomServiceImpl roomServiceImpl;

    private Room room;
    private RoomDto roomDto;

    @BeforeEach
    public void setup() {
        room = new Room();
        room.setId(1L);
        room.setRoomNumber("101");
        room.setRoomType(RoomType.DOUBLE);
        room.setCapacity(2);
        room.setMaxExtraBeds(1);
        room.setBookings(new ArrayList<>());

        roomDto = new RoomDto();
        roomDto.setId(1L);
        roomDto.setRoomNumber("101");
        roomDto.setRoomType(RoomType.DOUBLE);
        roomDto.setCapacity(2);
        roomDto.setMaxExtraBeds(1);
    }





    @Test
    public void getRoomByIdTest() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(roomMapper.toDto(room)).thenReturn(roomDto);

        RoomDto result = roomServiceImpl.getRoomById(1L);

        assertNotNull(result);
        assertEquals("101", result.getRoomNumber());
        assertEquals(RoomType.DOUBLE, result.getRoomType());
        assertEquals(2, result.getCapacity());
        verify(roomRepository).findById(1L);
    }

    @Test
    public void getRoomByIdTest_NotFound() {
        when(roomRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RoomNotFoundException.class, () -> roomServiceImpl.getRoomById(99L));
        verify(roomRepository).findById(99L);
    }

    @Test
    public void getAllRoomsTest() {
        List<Room> rooms = List.of(room);
        List<RoomDto> roomDtos = List.of(roomDto);

        when(roomRepository.findAll()).thenReturn(rooms);
        when(roomMapper.toDto(room)).thenReturn(roomDto);

        List<RoomDto> result = roomServiceImpl.getAllRooms();

        assertEquals(1, result.size());
        assertEquals("101", result.get(0).getRoomNumber());
        verify(roomRepository).findAll();
    }


    @Test
    public void findAvailableRoomsTest_NoBookings() {
        LocalDate start = LocalDate.of(2025, 5, 1);
        LocalDate end = LocalDate.of(2025, 5, 5);
        int guests = 2;

        when(roomRepository.findAll()).thenReturn(List.of(room));
        when(bookingRepository.findConflictingBookings(room.getId(), start, end))
                .thenReturn(Collections.emptyList());
        when(roomMapper.toDto(room)).thenReturn(roomDto);

        List<RoomDto> available = roomServiceImpl.findAvailableRoomFromQuery(start, end, guests);

        assertEquals(1, available.size());
        assertEquals("101", available.get(0).getRoomNumber());
    }


    @Test
    public void findAvailableRoomsTest_WithOverlappingBooking() {
        Booking booking = new Booking();
        booking.setCheckIn(LocalDate.of(2025, 5, 2));
        booking.setCheckOut(LocalDate.of(2025, 5, 6));

        when(roomRepository.findAll()).thenReturn(List.of(room));
        when(bookingRepository.findConflictingBookings(room.getId(),
                LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 5)))
                .thenReturn(List.of(booking));

        List<RoomDto> available = roomServiceImpl.findAvailableRoomFromQuery(
                LocalDate.of(2025, 5, 1),
                LocalDate.of(2025, 5, 5),
                2
        );

        assertTrue(available.isEmpty());
        verify(bookingRepository).findConflictingBookings(room.getId(),
                LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 5));
    }


    @Test
    public void findAvailableRoomsTest_CapacityTooLow() {
        int guests = 10;
        LocalDate start = LocalDate.of(2025, 6, 1);
        LocalDate end = LocalDate.of(2025, 6, 3);

        when(roomRepository.findAll()).thenReturn(List.of(room));

        List<RoomDto> result = roomServiceImpl.findAvailableRoomFromQuery(start, end, guests);

        assertTrue(result.isEmpty());
    }



    @Test
    public void findAvailableRoomsTest_EnoughExtraBeds() {
        room.setMaxExtraBeds(2);
        room.setCapacity(4); // 2 + 2
        roomDto.setMaxExtraBeds(2);
        roomDto.setCapacity(4);

        int guests = 4;
        LocalDate start = LocalDate.of(2025, 7, 1);
        LocalDate end = LocalDate.of(2025, 7, 5);

        when(roomRepository.findAll()).thenReturn(List.of(room));
        when(bookingRepository.findConflictingBookings(room.getId(), start, end))
                .thenReturn(Collections.emptyList());
        when(roomMapper.toDto(room)).thenReturn(roomDto);

        List<RoomDto> available = roomServiceImpl.findAvailableRoomFromQuery(start, end, guests);

        assertEquals(1, available.size());
        assertEquals("101", available.get(0).getRoomNumber());
    }


    @Test
    void findAvailableRoomFromQueryTest() {
        LocalDate checkIn = LocalDate.now().plusDays(5);
        LocalDate checkOut = LocalDate.now().plusDays(7);
        int guests = 2;

        when(roomRepository.findAll()).thenReturn(List.of(room));
        when(bookingRepository.findConflictingBookings(room.getId(), checkIn, checkOut))
                .thenReturn(Collections.emptyList());
        when(roomMapper.toDto(room)).thenReturn(roomDto);

        List<RoomDto> result = roomServiceImpl.findAvailableRoomFromQuery(checkIn, checkOut, guests);

        assertEquals(1, result.size());
        assertEquals(roomDto, result.get(0));

        verify(roomRepository).findAll();
        verify(bookingRepository).findConflictingBookings(room.getId(), checkIn, checkOut);
        verify(roomMapper).toDto(room);
    }

}
