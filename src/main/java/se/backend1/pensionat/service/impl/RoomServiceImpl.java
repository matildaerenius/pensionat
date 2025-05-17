package se.backend1.pensionat.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.backend1.pensionat.dto.RoomDto;
import se.backend1.pensionat.entity.Room;
import se.backend1.pensionat.mapper.RoomMapper;
import se.backend1.pensionat.model.RoomType;
import se.backend1.pensionat.repository.RoomRepository;
import se.backend1.pensionat.service.RoomService;

import java.util.List;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    @Override
    public Room saveRoom(Room room){
        validateExtraBeds(room);
        return roomRepository.save(room);

    }

    //Ny metod där det läggs till
    //Från frontend!
    @Override
    public Room saveRoomFromFrontEnd(RoomDto roomDto) {
        validateExtraBedsDto(roomDto);
        return roomMapper.roomDtoToRoom(roomDto);
    }

    @Override
    public List<RoomDto> getAllRooms() {
      // return roomRepository.findAll(); Är detta inte vad som är korrekt?

        List<Room> rooms = roomRepository.findAll();
        return rooms.stream()
                .map(roomMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Room createRoom(RoomDto roomDto) {
     Room room= roomMapper.roomDtoToRoom(roomDto);
     validateExtraBeds(room);
     return roomRepository.save(room);
    }

    @Override
    public RoomDto createRoomDto(RoomDto dto) {
        Room room = RoomMapper.toEntity(dto);
        validateExtraBeds(room); //  VG: validate extra bed rules
        Room saved = roomRepository.save(room);
        return roomMapper.toDto(saved);

    }

    @Override
    public RoomDto updateRoom(Long id, RoomDto dto) {
        roomRepository.findById(id.longValue())
                .orElseThrow(() -> new IllegalArgumentException("Rum med ID " + id + " hittades inte"));
        Room updatedRoom = RoomMapper.toEntity(dto);
        updatedRoom.setId(id);
        validateExtraBeds(updatedRoom);
        Room saved = roomRepository.save(updatedRoom);
        return roomMapper.toDto(saved);


    }

    @Override
    public void deleteRoom(Long id) {
        Room room = roomRepository.findById(id.longValue()).orElseThrow(() -> new IllegalArgumentException("Rum med ID " + id + " hittades inte"));

        if(!room.getBookings().isEmpty()) {
            throw new IllegalStateException("Rummet har bokningar och kan inte tas bort");
        }
            roomRepository.delete(room);
    }

    @Override
    public RoomDto getRoomById(Long id) {
        Room room = roomRepository.findById(id.longValue())
                .orElseThrow(() -> new IllegalArgumentException("Rum med ID " + id + " hittades inte"));

        return roomMapper.toDto(room);
    }


    // Valideringsmetod som kontrollerar att bara dubbelrum får ha extrasängar
    private void validateExtraBeds(Room room) {
        if (room.getRoomType() != RoomType.DOUBLE && room.getMaxExtraBeds() > 0) {
            throw new IllegalArgumentException("Extrasängar är endast tillåtna för dubbelrum");
        }

        if (room.getRoomType() == RoomType.SINGLE && room.getMaxExtraBeds() != 0) {
            throw new IllegalArgumentException("Enkelrum får inte ha extrasängar");
        }
    }

    // Valideringsmetod som kontrollerar att bara dubbelrum får ha extrasängar
    private void validateExtraBedsDto(RoomDto roomDto) {
        if (roomDto.getRoomType() != RoomType.DOUBLE && roomDto.getMaxExtraBeds() > 0) {
            throw new IllegalArgumentException("Extrasängar är endast tillåtna för dubbelrum");
        }

        if (roomDto.getRoomType() == RoomType.SINGLE && roomDto.getMaxExtraBeds() != 0) {
            throw new IllegalArgumentException("Enkelrum får inte ha extrasängar");
        }
    }
/*
    @Override
    public List<RoomDto> getAllRooms() {
        return List.of();
    }

    private RoomRepository roomRepository;

    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

 */
}
