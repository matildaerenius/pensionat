package se.backend1.pensionat.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.backend1.pensionat.dto.RoomDto;
import se.backend1.pensionat.entity.Booking;
import se.backend1.pensionat.entity.Room;
import se.backend1.pensionat.exception.RoomNotFoundException;
import se.backend1.pensionat.mapper.RoomMapper;
import se.backend1.pensionat.model.RoomType;
import se.backend1.pensionat.repository.BookingRepository;
import se.backend1.pensionat.repository.RoomRepository;
import se.backend1.pensionat.service.RoomService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final BookingRepository bookingRepository;

    @Override
    public RoomDto createRoom(RoomDto dto) {
        Room room = roomMapper.toEntity(dto);
        validateExtraBeds(room); //  VG: validate extra bed rules
        Room saved = roomRepository.save(room);
        return roomMapper.toDto(saved);
    }

    @Override
    public RoomDto updateRoom(Long id, RoomDto dto) {
        Room updatingRoom= roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException("Room not found with id: " + id));
        //alla fält i DTO som uppdateras
        updatingRoom.setRoomNumber(dto.getRoomNumber());
        updatingRoom.setRoomType(dto.getRoomType());
        updatingRoom.setMaxExtraBeds(dto.getMaxExtraBeds());
        updatingRoom.setCapacity(dto.getCapacity());

        validateExtraBeds(updatingRoom);

        Room saved = roomRepository.save(updatingRoom);
        return roomMapper.toDto(saved);
    }

    @Override
    public void deleteRoom(Long id) {
        Room room = roomRepository.findById(id).orElseThrow(() -> new RoomNotFoundException("Room not found with id: " + id));

        if(!room.getBookings().isEmpty()) {
            throw new IllegalStateException("Room has booking and cannot be deleted");
        }
        roomRepository.delete(room);
    }

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
    public List<RoomDto> findAvailableRooms(LocalDate checkIn, LocalDate checkOut, int guests) {
        List<Room> allRooms = roomRepository.findAll();
        List<RoomDto> availableRooms = new ArrayList<>();

        for (Room room : allRooms) {
            int totalCapacity = room.getCapacity() + room.getMaxExtraBeds();
            if (totalCapacity < guests) {
                continue;
            }

            boolean isAvailable = room.getBookings().stream().noneMatch(booking ->
                    !(checkOut.isBefore(booking.getCheckIn()) || checkIn.isAfter(booking.getCheckOut().minusDays(1)))
            );

            if (isAvailable) {
                availableRooms.add(roomMapper.toDto(room));
            }
        }

        return availableRooms;
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

    // Valideringsmetod som kontrollerar att bara dubbelrum får ha extrasängar
    private void validateExtraBeds(Room room) {
        if (room.getRoomType() != RoomType.DOUBLE && room.getMaxExtraBeds() > 0) {
            throw new IllegalArgumentException("Extra beds are only allowed for double rooms.");
        }

        if (room.getRoomType() == RoomType.SINGLE && room.getMaxExtraBeds() != 0) {
            throw new IllegalArgumentException("\n" +
                    "Single rooms are not allowed to have extra beds.");
        }
    }
    //tror ej denna används då vi kontroller med vanliga entitetsobjekt och inte DTO. Men vi låter vara kvar
    // Valideringsmetod som kontrollerar att bara dubbelrum får ha extrasängar
    private void validateExtraBedsDto(RoomDto roomDto) {
        if (roomDto.getRoomType() != RoomType.DOUBLE && roomDto.getMaxExtraBeds() > 0) {
            throw new IllegalArgumentException("Extrasängar är endast tillåtna för dubbelrum");
        }

        if (roomDto.getRoomType() == RoomType.SINGLE && roomDto.getMaxExtraBeds() != 0) {
            throw new IllegalArgumentException("Enkelrum får inte ha extrasängar");
        }
    }
}
