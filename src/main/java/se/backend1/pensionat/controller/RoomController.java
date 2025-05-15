package se.backend1.pensionat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import se.backend1.pensionat.service.RoomService;

@Controller
@RequestMapping("/rooms")
public class RoomController {

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
