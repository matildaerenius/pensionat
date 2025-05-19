package se.backend1.pensionat.service;

import se.backend1.pensionat.dto.RoomDto;

import java.time.LocalDate;
import java.util.List;

import se.backend1.pensionat.dto.RoomDto;
import se.backend1.pensionat.entity.Room;

import java.util.List;

public interface RoomService {
        // KLAR
        RoomDto createRoom(RoomDto dto);
        //KLAR
        RoomDto updateRoom(Long id, RoomDto dto);
        //KLAR
        void deleteRoom(Long id);
        //KLAR
        RoomDto getRoomById(Long id);
        //KLAR
        List<RoomDto> getAllRooms();
        //KLAR
        List<RoomDto> findAvailableRooms(LocalDate checkIn, LocalDate checkOut, int guests);
}
