package se.backend1.pensionat.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import se.backend1.pensionat.dto.RoomDto;
import se.backend1.pensionat.entity.Room;
import se.backend1.pensionat.service.BookingService;
import se.backend1.pensionat.service.CustomerService;
import se.backend1.pensionat.service.RoomService;

import java.time.LocalDate;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import se.backend1.pensionat.service.RoomService;

@Controller
@RequiredArgsConstructor //detta gör att vi kan ta bort Autowired o slipper göra konstruktorer
@RequestMapping("/rooms")
public class RoomController {

    private final BookingService bookingService;
    private final CustomerService customerService;
    private final RoomService roomService;

    @GetMapping
    public String listRooms(Model model) {
        List<RoomDto> rooms = roomService.getAllRooms();
        model.addAttribute("rooms", rooms);
        return "room-list";
    }

    @GetMapping("/search")
    public String searchAvailableRooms(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                       @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                       @RequestParam("guests") int guests,
                                       Model model) {

        List<RoomDto> availableRooms = roomService.findAvailableRooms(startDate, endDate, guests);
        model.addAttribute("availableRooms", availableRooms);
        return "search";
    }

    @GetMapping()
    public List<RoomDto> getAllRooms() {
       return getAllRooms();
       //kommer från roomService
    }
    @GetMapping("/occupied")
    public List<RoomDto> getAllOccupiedRooms() {
        return null ;
    }

    @GetMapping
    public String listRooms() {
        return null;
    }

    @GetMapping("/create")
    public String showCreateForm() {
        return null;
    }

    @PostMapping("/create")
    public String createRoom() {
        return null;
     }

     @GetMapping("/edit/{id}")
    public String showEditFrom() {
        return null;
     }

     @PostMapping("/edit/{id}")
    public String updateRoom() {
        return null;
     }

     @PostMapping("/delete/{id}")
    public String deleteRoom() {
        return null;
     }
}
