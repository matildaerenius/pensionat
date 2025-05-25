package se.backend1.pensionat.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.backend1.pensionat.dto.RoomDto;
import se.backend1.pensionat.entity.Booking;
import se.backend1.pensionat.entity.Room;
import se.backend1.pensionat.exception.InvalidRoomConfigurationException;
import se.backend1.pensionat.exception.RoomHasBookingsException;
import se.backend1.pensionat.exception.RoomNotFoundException;
import se.backend1.pensionat.exception.RoomNumberAlreadyExistsException;
import se.backend1.pensionat.mapper.RoomMapper;
import se.backend1.pensionat.model.RoomType;
import se.backend1.pensionat.repository.BookingRepository;
import se.backend1.pensionat.repository.RoomRepository;
import se.backend1.pensionat.service.RoomService;

import java.time.LocalDate;
import java.util.List;

import java.util.Optional;
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

    @Override
    public RoomDto createRoom(RoomDto roomDto) {
        Optional<Room> existingRoom = roomRepository.findByRoomNumber(roomDto.getRoomNumber());
        if (existingRoom.isPresent()) {
            throw new RoomNumberAlreadyExistsException("Rumsnumret är redan taget");
        }

        validateRoomConfiguration(roomDto);

        Room room = roomMapper.toEntity(roomDto);
        Room saved = roomRepository.save(room);
        return roomMapper.toDto(saved);
    }


    @Override
    public void updateRoom(Long id, RoomDto dto) {
        validateRoomConfiguration(dto);

        Room existingRoom = roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException("Room not found with id: " + id));

        existingRoom.setRoomNumber(dto.getRoomNumber());
        existingRoom.setRoomType(dto.getRoomType());
        existingRoom.setMaxExtraBeds(dto.getMaxExtraBeds());
        existingRoom.setAllowExtraBeds(dto.isAllowExtraBeds());
        existingRoom.setCapacity(dto.getCapacity());

        roomRepository.save(existingRoom);
    }


    @Override
    public void deleteRoom(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException("Room not found with id: " + id));

        if (!room.getBookings().isEmpty()) {
            throw new RoomHasBookingsException("Rummet har bokningar och kan inte raderas");
        }

        roomRepository.delete(room);
    }


    private void validateRoomConfiguration(RoomDto dto) {
        if (dto.getRoomType() != null) {
            switch (dto.getRoomType()) {
                case SINGLE -> {
                    if (dto.getMaxExtraBeds() > 0) {
                        throw new InvalidRoomConfigurationException("Enkelrum får inte ha extrasängar");
                    }
                    if (dto.getCapacity() != 1) {
                        throw new InvalidRoomConfigurationException("Enkelrum måste ha kapacitet 1");
                    }
                }
                case DOUBLE -> {
                    if (dto.getMaxExtraBeds() > 2) {
                        throw new InvalidRoomConfigurationException("Dubbelrum får ha max 2 extrasängar.");
                    }
                    int expectedCapacity = 2 + dto.getMaxExtraBeds();
                    if (dto.getCapacity() != expectedCapacity) {
                        throw new InvalidRoomConfigurationException(
                                "Kapacitet för dubbelrum med " + dto.getMaxExtraBeds() + " extrasängar, måste vara " + expectedCapacity);
                    }
                }
            }
        }
    }
}
