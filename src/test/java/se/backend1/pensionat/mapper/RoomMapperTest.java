package se.backend1.pensionat.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.backend1.pensionat.dto.RoomDto;
import se.backend1.pensionat.entity.Room;
import se.backend1.pensionat.model.RoomType;

import static org.junit.jupiter.api.Assertions.*;

class RoomMapperTest {

    private RoomMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new RoomMapper();
    }

    @Test
    void shouldReturnNullIfDtoIsNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void shouldReturnNullIfEntityIsNull() {
        assertNull(mapper.toDto(null));
    }

    @Test
    void shouldMapDtoToEntityCorrectly() {
        RoomDto dto = new RoomDto();
        dto.setId(1L);
        dto.setRoomNumber("101");
        dto.setRoomType(RoomType.DOUBLE);
        dto.setCapacity(2);
        dto.setAllowExtraBeds(true);
        dto.setMaxExtraBeds(1);

        Room room = mapper.toEntity(dto);
        assertNotNull(room);
        assertEquals(dto.getId(), room.getId());
        assertEquals(dto.getRoomNumber(), room.getRoomNumber());
        assertEquals(dto.getRoomType(), room.getRoomType());
        assertEquals(dto.getCapacity(), room.getCapacity());
        assertTrue(room.isAllowExtraBeds());
        assertEquals(dto.getMaxExtraBeds(), room.getMaxExtraBeds());
    }

    @Test
    void shouldMapEntityToDtoCorrectly() {
        Room room = new Room();
        room.setId(2L);
        room.setRoomNumber("202");
        room.setRoomType(RoomType.SINGLE);
        room.setCapacity(1);
        room.setAllowExtraBeds(false);
        room.setMaxExtraBeds(0);

        RoomDto dto = mapper.toDto(room);
        assertNotNull(dto);
        assertEquals(room.getId(), dto.getId());
        assertEquals(room.getRoomNumber(), dto.getRoomNumber());
        assertEquals(room.getRoomType(), dto.getRoomType());
        assertEquals(room.getCapacity(), dto.getCapacity());
        assertFalse(dto.isAllowExtraBeds());
        assertEquals(room.getMaxExtraBeds(), dto.getMaxExtraBeds());
    }
}
