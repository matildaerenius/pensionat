package se.backend1.pensionat.mapper;

import org.springframework.stereotype.Component;
import se.backend1.pensionat.dto.RoomDto;
import se.backend1.pensionat.entity.Room;

import static se.backend1.pensionat.entity.Room.*;

//Component görs så det inte blir error i roomserviceimpl
@Component
public class RoomMapper {


    // room till roomDTO
    public RoomDto roomToRoomDto(Room room) {
        return RoomDto.builder()
                .id(room.getId())
                .roomNumber(room.getRoomNumber())
                .roomType(room.getRoomType())
                .capacity().allowExtraBeds().maxExtraBeds().build();
    }

    // roomDTO till room
    public Room roomDtoToRoom(RoomDto roomDto) {
        return builder()
                .id(roomDto.getId())
                .roomNumber(roomDto.getRoomNumber())
                .roomType(roomDto.getRoomType())
                .capacity().allowExtraBeds().maxExtraBeds().build();
    }

//    public static Room toEntity(RoomDto dto) {
//        if (dto == null) return null;
//        Room room = new Room();
//        room.setId(dto.getId());
//        room.setRoomNumber(dto.getRoomNumber());
//        room.setRoomType(dto.getRoomType());
//        room.setCapacity(dto.getCapacity());
//        room.setMaxExtraBeds(dto.getMaxExtraBeds());
//        return room;
//    }
//
//    public static RoomDto toDto(Room room) {
//        if (room == null) return null;
//        RoomDto dto = new RoomDto();
//        dto.setId(room.getId());
//        dto.setRoomNumber(room.getRoomNumber());
//        dto.setRoomType(room.getRoomType());
//        dto.setCapacity(room.getCapacity());
//        dto.setMaxExtraBeds(room.getMaxExtraBeds());
//        return dto;
//    }
}
