package se.backend1.pensionat.service.impl;

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
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;


    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository, RoomMapper roomMapper) {
        this.roomRepository = roomRepository;
        this.roomMapper=roomMapper;

    }

    @Override
    public Room saveRoom(Room room){
        //If the room is not a DOUBLE room but still has extra beds set (maxExtraBeds > 0), then that’s not allowed.
        if(room.getRoomType()!=RoomType.DOUBLE&& room.getMaxExtraBeds()>0){
            throw new IllegalArgumentException("Extrasängar är endast tillåtna för dubbelrum");
        }
        return roomRepository.save(room);
    }
    @Override
    public List<RoomDto> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        return rooms.stream()
                .map(roomMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public RoomDto createRoom(RoomDto dto) {
        Room room = roomMapper.toEntity(dto);
        Room saved = roomRepository.save(room);
        return roomMapper.toDto(saved);
    }

    @Override
    public RoomDto updateRoom(Long id, RoomDto dto) {
        return null;
    }

    @Override
    public void deleteRoom(Long id) {

    }

    @Override
    public RoomDto getRoomById(Long id) {
        return null;
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
