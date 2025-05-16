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


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import se.backend1.pensionat.service.RoomService;

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


//    private RoomService roomService;
//
//    @Autowired
//    public RoomController(RoomService roomService) {
//        this.roomService = roomService;
//    }
//
//    @GetMapping
//    public String listRooms() {
//        return null;
//    }
//
//    @GetMapping("/create")
//    public String showCreateForm() {
//        return null;
//    }
//
//    @PostMapping("/create")
//    public String createRoom() {
//        return null;
//     }
//
//     @GetMapping("/edit/{id}")
//    public String showEditFrom() {
//        return null;
//     }
//
//     @PostMapping("/edit/{id}")
//    public String updateRoom() {
//        return null;
//     }
//
//     @PostMapping("/delete/{id}")
//    public String deleteRoom() {
//        return null;
//     }
}
