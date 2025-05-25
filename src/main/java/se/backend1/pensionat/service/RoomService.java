package se.backend1.pensionat.service;

import jakarta.validation.Valid;
import se.backend1.pensionat.dto.RoomDto;

import java.time.LocalDate;
import java.util.List;

public interface RoomService {

        RoomDto getRoomById(Long id);

        List<RoomDto> getAllRooms();

        List<RoomDto> findAvailableRoomFromQuery(LocalDate checkIn, LocalDate checkOut, int guests);

        RoomDto createRoom(@Valid RoomDto dto);

        void updateRoom(Long id, @Valid RoomDto dto);

        void deleteRoom(Long id);
}
