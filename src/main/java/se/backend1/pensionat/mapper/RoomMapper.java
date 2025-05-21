package se.backend1.pensionat.mapper;

import org.springframework.stereotype.Component;
import se.backend1.pensionat.dto.RoomDto;
import se.backend1.pensionat.entity.Room;

@Component
public class RoomMapper {
    public RoomDto toDto(Room room) {
        if (room == null) return null;

        return RoomDto.builder()
                .id(room.getId())
                .roomNumber(room.getRoomNumber())
                .roomType(room.getRoomType())
                .capacity(room.getCapacity())
                .allowExtraBeds(room.isAllowExtraBeds())
                .maxExtraBeds(room.getMaxExtraBeds())
                .build();
    }

    public Room toEntity(RoomDto dto) {
        if (dto == null) return null;

        return Room.builder()
                .id(dto.getId())
                .roomNumber(dto.getRoomNumber())
                .roomType(dto.getRoomType())
                .capacity(dto.getCapacity())
                .allowExtraBeds(dto.isAllowExtraBeds())
                .maxExtraBeds(dto.getMaxExtraBeds())
                .build();
    }
}


//    // room till roomDTO
//    public RoomDto toDto(Room room) {
//        return RoomDto.builder()
//                .id(room.getId())
//                .roomNumber(room.getRoomNumber())
//                .roomType(room.getRoomType())
//                .capacity().allowExtraBeds().maxExtraBeds().build();
//    }
//
//    // roomDTO till room
//    public Room toEntity(RoomDto roomDto) {
//        return Room.builder()
//                .id(roomDto.getId())
//                .roomNumber(roomDto.getRoomNumber())
//                .roomType(roomDto.getRoomType())
//                .capacity().allowExtraBeds().maxExtraBeds().build();
//    }
//
//}
