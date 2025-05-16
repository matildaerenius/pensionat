package se.backend1.pensionat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.backend1.pensionat.dto.RoomDto;
import se.backend1.pensionat.entity.Room;
import se.backend1.pensionat.mapper.RoomMapper;
import se.backend1.pensionat.repository.RoomRepository;
import se.backend1.pensionat.service.RoomService;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    private RoomRepository roomRepository;

    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public RoomDto createRoom(RoomDto dto) {
        Room room = RoomMapper.toEntity(dto);
        Room saved = roomRepository.save(room);
        return RoomMapper.toDto(saved);
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

    @Override
    public List<RoomDto> getAllRooms() {
        return List.of();
    }
}
