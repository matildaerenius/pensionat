package se.backend1.pensionat.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import se.backend1.pensionat.dto.BookingDto;
import se.backend1.pensionat.dto.RoomDto;
import se.backend1.pensionat.service.BookingService;
import se.backend1.pensionat.service.CustomerService;
import se.backend1.pensionat.service.RoomService;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final CustomerService customerService;
    private final RoomService roomService;


    @GetMapping
    public String getAllBookings(Model model){
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "bookings/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("bookingDto", new BookingDto());
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("rooms", roomService.getAllRooms());
        return "bookings/create";
    }

    @PostMapping("/create")
    public String createBooking(@ModelAttribute("bookingDto") @Valid BookingDto bookingDto,
                                BindingResult result,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        if (result.hasErrors()) {
            model.addAttribute("customers", customerService.getAllCustomers());
            model.addAttribute("rooms", roomService.getAllRooms());
            return "bookings/create";
        }
        bookingService.createBooking(bookingDto);
        redirectAttributes.addFlashAttribute("success", "Bokning skapad!");
        return "redirect:/bookings";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        BookingDto dto = bookingService.getBookingById(id);
        model.addAttribute("bookingDto", dto);
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("rooms", roomService.getAllRooms());
        return "bookings/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateBooking(@PathVariable Long id,
                                @ModelAttribute("bookingDto") @Valid BookingDto bookingDto,
                                BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
            model.addAttribute("customers", customerService.getAllCustomers());
            model.addAttribute("rooms", roomService.getAllRooms());
            return "bookings/edit";
        }
        bookingService.updateBooking(id, bookingDto);
        return "redirect:/bookings";
    }

    @PostMapping("/delete/{id}")
    public String deleteBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bookingService.deleteBooking(id);
            redirectAttributes.addFlashAttribute("success", "Bokning raderad!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Det gick inte att ta bort bokningen.");
        }
        return "redirect:/bookings";
    }

    @GetMapping("/search")
    public String showSearchForm() {
        return "bookings/search";
    }

    @PostMapping("/edit/{id}")
    public String editBooking(@PathVariable Long id){
        return null;
    }

    @GetMapping("/search-results")
    public String getAvailableRooms(@RequestParam LocalDate checkIn,
                                    @RequestParam LocalDate checkOut,
                                    @RequestParam int guests,
                                    Model model) {
        List<RoomDto> availableRooms = roomService.findAvailableRooms(checkIn, checkOut, guests);
        model.addAttribute("availableRooms", availableRooms);
        return "bookings/search-results";
    }
}

