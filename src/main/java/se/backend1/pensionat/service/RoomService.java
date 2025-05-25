package se.backend1.pensionat.service;

import se.backend1.pensionat.dto.RoomDto;

import java.time.LocalDate;
import java.util.List;

public interface RoomService {

        RoomDto getRoomById(Long id);

        List<RoomDto> getAllRooms();

        List<RoomDto> findAvailableRoomFromQuery(LocalDate checkIn, LocalDate checkOut, int guests);

}
