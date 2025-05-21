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
import se.backend1.pensionat.repository.CustomerRepository;
import se.backend1.pensionat.repository.RoomRepository;
import se.backend1.pensionat.service.impl.CustomerServiceImpl;
import se.backend1.pensionat.service.impl.RoomServiceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
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
    public void createRoomTest() {
        when(roomMapper.toEntity(roomDto)).thenReturn(room);
        when(roomRepository.save(room)).thenReturn(room);
        when(roomMapper.toDto(room)).thenReturn(roomDto);

        RoomDto result = roomServiceImpl.createRoom(roomDto);

        assertNotNull(result);
        assertEquals("101", result.getRoomNumber());
        assertEquals(RoomType.DOUBLE, result.getRoomType());
        assertEquals(1, result.getMaxExtraBeds());
        verify(roomRepository).save(room);
    }

    @Test
    public void updateRoomTest() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(roomRepository.save(any(Room.class))).thenReturn(room);
        when(roomMapper.toDto(any(Room.class))).thenReturn(roomDto);

        RoomDto updatedDto = new RoomDto();
        updatedDto.setRoomNumber("102");
        updatedDto.setRoomType(RoomType.DOUBLE);
        updatedDto.setCapacity(3);
        updatedDto.setMaxExtraBeds(1);

        RoomDto result = roomServiceImpl.updateRoom(1L, updatedDto);

        assertNotNull(result);
        assertEquals("101", result.getRoomNumber()); // Obs: du kan ändra denna om din mapper returnerar ny info
        assertEquals(RoomType.DOUBLE, result.getRoomType());
        assertTrue(result.getCapacity() >= 2);
        verify(roomRepository).save(any(Room.class));
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
    public void deleteRoomTest_Success() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        roomServiceImpl.deleteRoom(1L);

        verify(roomRepository).delete(room);
    }

    @Test
    public void deleteRoomTest_error() {
        room.getBookings().add(new se.backend1.pensionat.entity.Booking());
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        assertThrows(IllegalStateException.class, () -> roomServiceImpl.deleteRoom(1L));
    }

    @Test
    public void findAvailableRoomsTest_NoBookings() {
        List<Room> rooms = List.of(room);
        when(roomRepository.findAll()).thenReturn(rooms);
        when(roomMapper.toDto(any(Room.class))).thenReturn(roomDto);

        LocalDate start = LocalDate.of(2025, 5, 1);
        LocalDate end = LocalDate.of(2025, 5, 5);

        List<RoomDto> available = roomServiceImpl.findAvailableRooms(start, end, 2);

        assertEquals(1, available.size());
        assertEquals("101", available.get(0).getRoomNumber());
    }

    @Test
    public void findAvailableRoomsTest_WithOverlappingBooking() {
        Booking booking = new Booking();
        booking.setCheckIn(LocalDate.of(2025, 5, 2));
        booking.setCheckOut(LocalDate.of(2025, 5, 6));
        room.getBookings().add(booking);

        when(roomRepository.findAll()).thenReturn(List.of(room));

        LocalDate start = LocalDate.of(2025, 5, 1);
        LocalDate end = LocalDate.of(2025, 5, 5);

        List<RoomDto> available = roomServiceImpl.findAvailableRooms(start, end, 2);

        assertTrue(available.isEmpty());
    }

    @Test
    public void findAvailableRoomsTest_CapacityTooLow() {
        when(roomRepository.findAll()).thenReturn(List.of(room));

        LocalDate start = LocalDate.of(2025, 6, 1);
        LocalDate end = LocalDate.of(2025, 6, 3);

        // Kräver fler platser än rum + extrabäddar
        List<RoomDto> available = roomServiceImpl.findAvailableRooms(start, end, 10);

        assertTrue(available.isEmpty());
    }

    @Test
    public void findAvailableRoomsTest_EnoughExtraBeds() {
        room.setCapacity(2);
        room.setMaxExtraBeds(2);
        when(roomRepository.findAll()).thenReturn(List.of(room));
        when(roomMapper.toDto(room)).thenReturn(roomDto);

        LocalDate start = LocalDate.of(2025, 7, 1);
        LocalDate end = LocalDate.of(2025, 7, 5);

        List<RoomDto> available = roomServiceImpl.findAvailableRooms(start, end, 4); // kräver exakt 4 sängplatser

        assertEquals(1, available.size());
        assertEquals("101", available.get(0).getRoomNumber());
    }


}
