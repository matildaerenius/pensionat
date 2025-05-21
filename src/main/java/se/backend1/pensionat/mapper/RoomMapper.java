package se.backend1.pensionat.mapper;

import org.springframework.stereotype.Component;
import se.backend1.pensionat.dto.RoomDto;
import se.backend1.pensionat.entity.Room;

@Component
public class RoomMapper {


    // room till roomDTO
    public RoomDto toDto(Room room) {
        return RoomDto.builder()
                .id(room.getId())
                .roomNumber(room.getRoomNumber())
                .roomType(room.getRoomType())
                .allowExtraBeds(room.isAllowExtraBeds())
                .build();
    }

    // roomDTO till room
    public Room toEntity(RoomDto roomDto) {
        return Room.builder()
                .id(roomDto.getId())
                .roomNumber(roomDto.getRoomNumber())
                .roomType(roomDto.getRoomType())
                .build();
    }

}
