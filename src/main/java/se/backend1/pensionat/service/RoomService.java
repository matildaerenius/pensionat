package se.backend1.pensionat.service;

import se.backend1.pensionat.dto.RoomDto;

import java.util.List;

import se.backend1.pensionat.dto.RoomDto;
import se.backend1.pensionat.entity.Room;

import java.util.List;

public interface RoomService {
        Room saveRoom(Room room);
        List<RoomDto> getAllRooms();


    RoomDto createRoom(RoomDto dto);

    RoomDto updateRoom(Long id, RoomDto dto);

    void deleteRoom(Long id);

    RoomDto getRoomById(Long id);

    List<RoomDto> getAllRooms();
}
