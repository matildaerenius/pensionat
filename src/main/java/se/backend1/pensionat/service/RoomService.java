package se.backend1.pensionat.service;

import se.backend1.pensionat.dto.RoomDto;

import java.time.LocalDate;
import java.util.List;

import se.backend1.pensionat.dto.RoomDto;
import se.backend1.pensionat.entity.Room;

import java.util.List;

public interface RoomService {
        //tror klar
        RoomDto createRoomDto(RoomDto dto);

        RoomDto updateRoom(Long id, RoomDto dto);

        void deleteRoom(Long id);

        RoomDto getRoomById(Long id);

        List<RoomDto> getAllRooms();

        List<RoomDto> findAvailableRooms(LocalDate checkIn, LocalDate checkOut, int guests);




        Room saveRoom(Room room);
        //Tittar rad 15 och rad 35 i impl
        Room saveRoomFromFrontEnd(RoomDto roomDto);

        Room createRoom(RoomDto dto);



}
