package se.backend1.pensionat.mapper;

import org.springframework.stereotype.Component;
import se.backend1.pensionat.dto.RoomDto;
import se.backend1.pensionat.entity.Room;
import se.backend1.pensionat.model.RoomType;

@Component
public class RoomMapper {


    // room till roomDTO
    public RoomDto toDto(Room room) {
        return RoomDto.builder()
                .id(room.getId())
                .roomNumber(room.getRoomNumber())
                .roomType(room.getRoomType())
                .capacity(room.getCapacity())
                .maxExtraBeds(room.getMaxExtraBeds())
                .build();
    }

    // roomDTO till room
    public Room toEntity(RoomDto roomDto) {
        int adjustedCapacity = roomDto.getCapacity();

        if (roomDto.getRoomType() == RoomType.DOUBLE) {
            adjustedCapacity = 2 + roomDto.getMaxExtraBeds();
        }

        return Room.builder()
                .id(roomDto.getId())
                .roomNumber(roomDto.getRoomNumber())
                .roomType(roomDto.getRoomType())
                .allowExtraBeds(roomDto.isAllowExtraBeds())
                .capacity(adjustedCapacity)
                .maxExtraBeds(roomDto.getMaxExtraBeds())
                .build();
    }

}
