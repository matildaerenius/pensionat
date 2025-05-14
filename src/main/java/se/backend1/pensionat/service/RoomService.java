package se.backend1.pensionat.service;

import se.backend1.pensionat.dto.RoomDto;

import java.util.List;

public interface RoomService {

    RoomDto createRoom(RoomDto dto);

    RoomDto updateRoom(Long id, RoomDto dto);

    void deleteRoom(Long id);

    RoomDto getRoomById(Long id);

    List<RoomDto> getAllRooms();
}
