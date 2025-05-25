package se.backend1.pensionat.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.backend1.pensionat.dto.RoomDto;
import se.backend1.pensionat.entity.Booking;
import se.backend1.pensionat.entity.Room;
import se.backend1.pensionat.exception.RoomNotFoundException;
import se.backend1.pensionat.mapper.RoomMapper;
import se.backend1.pensionat.repository.BookingRepository;
import se.backend1.pensionat.repository.RoomRepository;
import se.backend1.pensionat.service.RoomService;

import java.time.LocalDate;
import java.util.List;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final BookingRepository bookingRepository;


    @Override
    public RoomDto getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException("Room not found with id: " + id));

        return roomMapper.toDto(room);
    }

    @Override
    public List<RoomDto> getAllRooms() {
        return roomRepository.findAll().stream().map(roomMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<RoomDto> findAvailableRoomFromQuery(LocalDate checkIn, LocalDate checkOut, int guests) {
        // Steg 1: Hitta rum med tillräcklig kapacitet
        List<Room> roomsWithCapacity = roomRepository.findByTotalCapacityGreaterThanEqual(guests);

        // Steg 2: Filtrera bort rum med konflikter
        List<Room> availableRooms = roomsWithCapacity.stream()
                .filter(room -> {
                    List<Booking> conflicts = bookingRepository.findConflictingBookings(
                            room.getId(), checkIn, checkOut);
                    return conflicts.isEmpty();
                })
                .collect(Collectors.toList());

        return availableRooms.stream()
                .map(roomMapper::toDto)
                .collect(Collectors.toList());
    }

}
