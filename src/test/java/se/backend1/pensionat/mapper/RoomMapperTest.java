package se.backend1.pensionat.mapper;

import org.junit.jupiter.api.Test;
import se.backend1.pensionat.dto.RoomDto;
import se.backend1.pensionat.entity.Room;
import se.backend1.pensionat.model.RoomType;

import static org.junit.jupiter.api.Assertions.*;

public class RoomMapperTest {

    @Test
    void shouldMapRoomDtoToEntityAndBack() {
        RoomDto dto = new RoomDto();
        dto.setId(1L);
        dto.setRoomNumber("101");
        dto.setRoomType(RoomType.DOUBLE);
        dto.setCapacity(2);
        dto.setMaxExtraBeds(1);

        Room entity = RoomMapper.toEntity(dto);
        RoomDto mappedBack = RoomMapper.toDto(entity);

        assertEquals(dto.getRoomNumber(), mappedBack.getRoomNumber());
        assertEquals(dto.getRoomType(), mappedBack.getRoomType());
        assertEquals(dto.getCapacity(), mappedBack.getCapacity());
        assertEquals(dto.getMaxExtraBeds(), mappedBack.getMaxExtraBeds());
    }
    @Test
    void shouldMapRoomEntityToDtoCorrectly() {
        Room room = new Room();
        room.setId(5L);
        room.setRoomNumber("B202");
        room.setRoomType(RoomType.SINGLE);
        room.setCapacity(1);
        room.setMaxExtraBeds(0);

        RoomDto dto = RoomMapper.toDto(room);

        assertNotNull(dto);
        assertEquals(room.getId(), dto.getId());
        assertEquals(room.getRoomNumber(), dto.getRoomNumber());
        assertEquals(room.getRoomType(), dto.getRoomType());
        assertEquals(room.getCapacity(), dto.getCapacity());
        assertEquals(room.getMaxExtraBeds(), dto.getMaxExtraBeds());
    }

    @Test
    void shouldReturnNullIfDtoIsNull() {
        assertNull(RoomMapper.toEntity(null));
    }

    @Test
    void shouldReturnNullIfEntityIsNull() {
        assertNull(RoomMapper.toDto(null));
    }
}


