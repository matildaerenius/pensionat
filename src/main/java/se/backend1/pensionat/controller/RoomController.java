package se.backend1.pensionat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.backend1.pensionat.dto.RoomDto;
import se.backend1.pensionat.entity.Room;
import se.backend1.pensionat.service.RoomService;

import java.util.List;

@Controller
@RequestMapping("/rooms")
public class RoomController {
    private RoomService roomService;
    private BookingController bookingController;


    @Autowired
    public RoomController(RoomService roomService, BookingController bookingController) {
        this.roomService = roomService;
        this.bookingController = bookingController;
    }

    @GetMapping()
    public List<RoomDto> getAllRooms() {
       return getAllRooms();
       //kommer från roomService
    }

    @GetMapping("/occupied")
    public List<RoomDto> getAllOccupiedRooms() {
        return ;
    }

}
