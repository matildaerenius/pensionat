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
            rooms = roomService.findAvailableRooms(start, end, guests);
        } else {
            rooms = roomService.getAllRooms();
        }

        model.addAttribute("rooms", rooms);
        return "rooms/list";
    }

    // TODO : Denna behövs inte om vi inte ska kunna skapa nya rum?
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("roomDto", new RoomDto());
        return "rooms/create";
    }

    @GetMapping("/occupied")
    public String showOccupiedRooms(Model model) {
        // Ej implementerat – kräver extra logik
        model.addAttribute("message", "Funktion ej klar");
        return "rooms/occupied";
    }

    // TODO : Denna behövs inte?
    @PostMapping("/create")
    public String createRoom(@ModelAttribute("roomDto") @Valid RoomDto roomDto,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "rooms/create";
        }
        roomService.createRoom(roomDto);
        redirectAttributes.addFlashAttribute("success", "Rum skapat!");
        return "redirect:/rooms";
    }

    // TODO : Denna behövs inte?
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        RoomDto roomDto = roomService.getRoomById(id);
        model.addAttribute("roomDto", roomDto);
        return "rooms/edit";
    }

    // TODO : Denna behövs inte?
    @PostMapping("/edit/{id}")
    public String updateRoom(@PathVariable Long id,
                             @ModelAttribute("roomDto") @Valid RoomDto roomDto,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "rooms/edit";
        }
        roomService.updateRoom(id, roomDto);
        redirectAttributes.addFlashAttribute("success", "Rummet har uppdaterats.");
        return "redirect:/rooms";
    }

    // TODO : Denna behövs inte?
    @PostMapping("/delete/{id}")
    public String deleteRoom(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            roomService.deleteRoom(id);
            redirectAttributes.addFlashAttribute("success", "Rummet raderat.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Rummet kunde inte tas bort.");
        }
        return "redirect:/rooms";
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
