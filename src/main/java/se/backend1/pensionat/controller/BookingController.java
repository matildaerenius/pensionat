package se.backend1.pensionat.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import se.backend1.pensionat.dto.BookingDto;
import se.backend1.pensionat.dto.DetailedBookingDto;
import se.backend1.pensionat.dto.RoomDto;
import se.backend1.pensionat.entity.Booking;
import se.backend1.pensionat.entity.Room;
import se.backend1.pensionat.mapper.BookingMapper;
import se.backend1.pensionat.repository.BookingRepository;
import se.backend1.pensionat.repository.RoomRepository;
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
    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;


    @GetMapping
    public String getAllBookings(Model model){
        List<DetailedBookingDto> detailedBookings = bookingService.getAllDetailedBookings();
        model.addAttribute("bookings", detailedBookings);
        return "bookings/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("bookingDto", new BookingDto());
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("rooms", roomService.getAllRooms());
        model.addAttribute("edit", false);
        model.addAttribute("formAction", "/bookings/create");
        return "bookings/form";
    }

    @PostMapping("/create")
    public String createBooking(@ModelAttribute("bookingDto") @Valid BookingDto bookingDto,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttributes) {

        if (bookingDto.getCheckIn().isAfter(bookingDto.getCheckOut())) {
            result.rejectValue("checkIn", "invalid", "Incheckning kan inte vara efter utcheckning.");
        }

        Room room = roomRepository.findById(bookingDto.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Rum finns inte"));

        int maxGuests = room.getCapacity() + (room.isAllowExtraBeds() ? room.getMaxExtraBeds() : 0);
        if (bookingDto.getNumberOfGuests() > maxGuests) {
            result.rejectValue("numberOfGuests", "invalid", "För många gäster för detta rum. Max: " + maxGuests);
        }

        // Om valideringsfel finns, visar formuläret igen
        if (result.hasErrors()) {
            model.addAttribute("customers", customerService.getAllCustomers());
            model.addAttribute("rooms", roomService.getAllRooms());
            model.addAttribute("edit", false);
            model.addAttribute("formAction", "/bookings/create");
            return "bookings/form";
        }

        // Dubbelbokningskontroll
        bookingService.validateNoDoubleBooking(room, bookingDto.getCheckIn(), bookingDto.getCheckOut());

        Booking booking = bookingMapper.toEntity(bookingDto);
        booking.setRoom(room);
        bookingRepository.save(booking);
        return "redirect:/bookings";
    }



    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("bookingDto", bookingService.getBookingById(id));
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("rooms", roomService.getAllRooms());
        model.addAttribute("edit", true);
        model.addAttribute("formAction", "/bookings/edit/" + id);
        return "bookings/form";
    }

    @PostMapping("/edit/{id}")
    public String updateBooking(@PathVariable Long id,
                                @ModelAttribute("bookingDto") @Valid BookingDto bookingDto,
                                BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
            model.addAttribute("customers", customerService.getAllCustomers());
            model.addAttribute("rooms", roomService.getAllRooms());
            model.addAttribute("edit", true);
            return "bookings/form";
        }
        bookingService.updateBooking(id, bookingDto);
        return "redirect:/bookings";
    }

    @PostMapping("/delete/{id}")
    public String deleteBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bookingService.deleteBooking(id);
            redirectAttributes.addFlashAttribute("success", "Bokning avbokad!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Det gick inte att ta bort bokningen.");
            e.printStackTrace();
        }
        return "redirect:/bookings";
    }

    @GetMapping("/search")
    public String showSearchForm() {
        return "bookings/search";
    }

//    @GetMapping("/edit/{id}")
//    public String editBooking(@PathVariable Long id, Model model) {
//        model.addAttribute("bookingDto", bookingService.getBookingById(id));
//        return "bookings/form";
//    }

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

