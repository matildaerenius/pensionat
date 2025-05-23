package se.backend1.pensionat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import se.backend1.pensionat.dto.RoomSearchDto;

@Controller
public class HomeController {

    @GetMapping("/")
    public String showHome(Model model) {
        model.addAttribute("roomSearchDto", new RoomSearchDto());
        return "index";
    }
}

