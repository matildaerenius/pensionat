package se.backend1.pensionat.mapper;

import org.junit.jupiter.api.Test;
import se.backend1.pensionat.dto.RoomDto;
import se.backend1.pensionat.entity.Room;
import se.backend1.pensionat.model.RoomType;

import static org.junit.jupiter.api.Assertions.*;

public class RoomMapperTest {

    private final RoomMapper roomMapper = new RoomMapper();

    @Test
    public void testToDto() {
        Room room = Room.builder()
                .id(1L)
                .roomNumber("101")
                .roomType(RoomType.DOUBLE)
                .build();

        RoomDto dto = roomMapper.toDto(room);

        assertEquals(room.getId(), dto.getId());
        assertEquals(room.getRoomNumber(), dto.getRoomNumber());
        assertEquals(room.getRoomType(), dto.getRoomType());
    }

    @Test
    public void testToEntity() {
        RoomDto dto = RoomDto.builder()
                .id(2L)
                .roomNumber("202")
                .roomType(RoomType.DOUBLE)
                .capacity(4)
                .maxExtraBeds(2)
                .allowExtraBeds(true)
                .build();

        Room room = roomMapper.toEntity(dto);

        assertEquals(dto.getId(), room.getId());
        assertEquals(dto.getRoomNumber(), room.getRoomNumber());
        assertEquals(dto.getRoomType(), room.getRoomType());
        assertEquals(4, room.getCapacity());
        assertEquals(dto.getMaxExtraBeds(), room.getMaxExtraBeds());
        assertEquals(dto.isAllowExtraBeds(), room.isAllowExtraBeds());
    }
}


