package se.backend1.pensionat.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import se.backend1.pensionat.dto.RoomDto;
import se.backend1.pensionat.dto.RoomSearchDto;
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

    @GetMapping("/search")
    public String showSearchForm(Model model) {
        model.addAttribute("roomSearchDto", new RoomSearchDto());
        return "index";
    }

    @PostMapping("/search")
    public String handleSearchForm(@ModelAttribute("roomSearchDto") @Valid RoomSearchDto dto,
                                   BindingResult result,
                                   Model model) {
        if (result.hasErrors()) {
            return "index";
        }

        var rooms = roomService.findAvailableRoomFromQuery(
                dto.getCheckIn(), dto.getCheckOut(), dto.getNumberOfGuests());

        model.addAttribute("availableRooms", rooms);
        model.addAttribute("checkIn", dto.getCheckIn());
        model.addAttribute("checkOut", dto.getCheckOut());
        model.addAttribute("guests", dto.getNumberOfGuests());

        return "rooms/available-rooms";
    }

}
