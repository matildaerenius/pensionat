package se.backend1.pensionat.mapper;

import se.backend1.pensionat.dto.RoomDto;
import se.backend1.pensionat.entity.Room;

public class RoomMapper {

    public static Room toEntity(RoomDto dto) {
        if (dto == null) return null;
        Room room = new Room();
        room.setId(dto.getId());
        room.setRoomNumber(dto.getRoomNumber());
        room.setRoomType(dto.getRoomType());
        room.setCapacity(dto.getCapacity());
        room.setMaxExtraBeds(dto.getMaxExtraBeds());
        return room;
    }

    public static RoomDto toDto(Room room) {
        if (room == null) return null;
        RoomDto dto = new RoomDto();
        dto.setId(room.getId());
        dto.setRoomNumber(room.getRoomNumber());
        dto.setRoomType(room.getRoomType());
        dto.setCapacity(room.getCapacity());
        dto.setMaxExtraBeds(room.getMaxExtraBeds());
        return dto;
    }
}
