package se.backend1.pensionat.service;

import se.backend1.pensionat.dto.RoomDto;
import se.backend1.pensionat.entity.Room;

import java.util.List;

public interface RoomService {
        Room saveRoom(Room room);
        List<RoomDto> getAllRooms();

    }
