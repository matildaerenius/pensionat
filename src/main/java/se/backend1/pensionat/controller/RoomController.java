package se.backend1.pensionat.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import se.backend1.pensionat.dto.RoomDto;
import se.backend1.pensionat.dto.RoomSearchDto;
import se.backend1.pensionat.exception.InvalidRoomConfigurationException;
import se.backend1.pensionat.exception.RoomHasBookingsException;
import se.backend1.pensionat.exception.RoomNotFoundException;
import se.backend1.pensionat.exception.RoomNumberAlreadyExistsException;
import se.backend1.pensionat.model.RoomType;
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

    @GetMapping("/create")
    public String showCreateRoomForm(Model model) {
        model.addAttribute("roomDto", new RoomDto());
        model.addAttribute("roomTypes", RoomType.values());
        model.addAttribute("formAction", "/rooms/create");
        model.addAttribute("edit", false);
        return "rooms/form";
    }

    // TODO : Nedan kan lite flyttas till service lagret
    @PostMapping("/create")
    public String createRoom(@ModelAttribute("roomDto") @Valid RoomDto roomDto,
                             BindingResult result,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("roomTypes", RoomType.values());
            model.addAttribute("edit", false);
            return "rooms/form";
        }

        try {
            roomService.createRoom(roomDto);
        } catch (RoomNumberAlreadyExistsException | InvalidRoomConfigurationException e) {
            model.addAttribute("roomTypes", RoomType.values());
            model.addAttribute("edit", false);
            model.addAttribute("error", e.getMessage());
            return "rooms/form";
        }

        return "redirect:/rooms";
    }



    @GetMapping("/edit/{id}")
    public String showEditRoomForm(@PathVariable Long id, Model model) {
        RoomDto dto = roomService.getRoomById(id);
        model.addAttribute("roomDto", dto);
        model.addAttribute("roomTypes", RoomType.values());
        model.addAttribute("formAction", "/rooms/edit/" + id);
        model.addAttribute("edit", true);
        return "rooms/form";
    }

    @PostMapping("/edit/{id}")
    public String updateRoom(@PathVariable Long id,
                             @Valid @ModelAttribute("roomDto") RoomDto dto,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("roomTypes", RoomType.values());
            model.addAttribute("formAction", "/rooms/edit/" + id);
            model.addAttribute("edit", true);
            return "rooms/form";
        }

        try {
            roomService.updateRoom(id, dto);
            redirectAttributes.addFlashAttribute("success", "Rum uppdaterat!");
            return "redirect:/rooms";
        } catch (InvalidRoomConfigurationException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("roomTypes", RoomType.values());
            model.addAttribute("formAction", "/rooms/edit/" + id);
            model.addAttribute("edit", true);
            return "rooms/form";
        }
    }


    @PostMapping("/delete/{id}")
    public String deleteRoom(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            roomService.deleteRoom(id);
            redirectAttributes.addFlashAttribute("cancel", "Rum borttaget!");
        } catch (RoomHasBookingsException e) {
            redirectAttributes.addFlashAttribute("error", "Rum har bokningar och kan inte tas bort");
        } catch (RoomNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "Rummet kunde inte hittas.");
        }
        return "redirect:/rooms";
    }

}
