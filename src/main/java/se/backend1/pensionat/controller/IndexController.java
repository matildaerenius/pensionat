package se.backend1.pensionat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import se.backend1.pensionat.dto.RoomDto;
import se.backend1.pensionat.service.RoomService;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final RoomService roomService;

    @GetMapping("/")
    public String showIndexSearchForm(@RequestParam(required = false) String checkIn,
                                      @RequestParam(required = false) String checkOut,
                                      @RequestParam(required = false) Integer guests,
                                      Model model) {

        List<RoomDto> result = null;

        if (checkIn != null && checkOut != null && guests != null) {
            LocalDate start = LocalDate.parse(checkIn);
            LocalDate end = LocalDate.parse(checkOut);
            result = roomService.findAvailableRooms(start, end, guests);
        }

        model.addAttribute("rooms", result);
        return "index";  // rendera index.html
    }
}
