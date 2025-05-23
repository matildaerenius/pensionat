package se.backend1.pensionat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import se.backend1.pensionat.dto.RoomDto;
import se.backend1.pensionat.service.RoomService;

import java.time.LocalDate;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;


    @GetMapping
    public String getAllRooms(@RequestParam(required = false) String checkIn,
                              @RequestParam(required = false) String checkOut,
                              @RequestParam(required = false) Integer guests,
                              Model model) {
        List<RoomDto> rooms;

        if (checkIn != null && checkOut != null && guests != null) {
            LocalDate start = LocalDate.parse(checkIn);
            LocalDate end = LocalDate.parse(checkOut);
            rooms = roomService.findAvailableRoomFromQuery(start, end, guests);
        } else {
            rooms = roomService.getAllRooms();
        }

        model.addAttribute("rooms", rooms);
        return "rooms/list";
    }


    @GetMapping("/available")
    public String getAvailableRooms(@RequestParam("checkIn") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
                                    @RequestParam("checkOut") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
                                    @RequestParam("guests") int guests,
                                    Model model) {
        List<RoomDto> availableRooms = roomService.findAvailableRoomFromQuery(checkIn, checkOut, guests);
        model.addAttribute("availableRooms", availableRooms);
        model.addAttribute("checkIn", checkIn);
        model.addAttribute("checkOut", checkOut);
        model.addAttribute("guests", guests);
        return "rooms/available-rooms";
    }

}
