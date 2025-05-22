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
import se.backend1.pensionat.exception.ActiveBookingDeletionException;
import se.backend1.pensionat.exception.BookingNotFoundException;
import se.backend1.pensionat.exception.CustomerHasBookingsException;
import se.backend1.pensionat.exception.RoomNotFoundException;
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
    public String showCreateForm(@RequestParam(required = false) LocalDate checkIn,
                                 @RequestParam(required = false) LocalDate checkOut,
                                 @RequestParam(required = false) Integer guests,
                                 Model model) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setCheckIn(checkIn);
        bookingDto.setCheckOut(checkOut);
        bookingDto.setNumberOfGuests(guests);

        model.addAttribute("bookingDto", bookingDto);
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("edit", false);
        model.addAttribute("formAction", "/bookings/create");

        if (checkIn != null && checkOut != null && guests != null) {
            model.addAttribute("rooms", roomService.findAvailableRoomFromQuery(checkIn, checkOut, guests));
        } else {
            model.addAttribute("rooms", List.of());
        }

        return "bookings/form";
    }

    @PostMapping("/create")
    public String createBooking(@ModelAttribute("bookingDto") @Valid BookingDto bookingDto,
                                BindingResult result,
                                RedirectAttributes redirectAttributes,
                                Model model) {


        if (bookingDto.getCheckIn().isAfter(bookingDto.getCheckOut())) {
            result.rejectValue("checkIn", "invalid", "Incheckning kan inte vara efter utcheckning.");
        }


        Room room = roomRepository.findById(bookingDto.getRoomId())
                .orElseThrow(() -> new RoomNotFoundException("Rum finns inte"));

        int maxGuests = room.getCapacity() + (room.isAllowExtraBeds() ? room.getMaxExtraBeds() : 0);
        if (bookingDto.getNumberOfGuests() > maxGuests) {
            result.rejectValue("numberOfGuests", "invalid", "För många gäster för detta rum. Max: " + maxGuests);
        }


        if (result.hasErrors()) {
            model.addAttribute("customers", customerService.getAllCustomers());
            model.addAttribute("rooms", roomService.getAllRooms());
            model.addAttribute("edit", false);
            model.addAttribute("formAction", "/bookings/create");
            return "bookings/form";
        }

        try {

            bookingService.save(bookingDto);
            redirectAttributes.addFlashAttribute("success", "Bokning skapad!");
            return "redirect:/bookings";
        } catch (RuntimeException e) {

            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("customers", customerService.getAllCustomers());
            model.addAttribute("rooms", roomService.getAllRooms());
            model.addAttribute("edit", false);
            model.addAttribute("formAction", "/bookings/create");
            return "bookings/form";
        }
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
            redirectAttributes.addFlashAttribute("cancel", "Bokning avbokad!");
        } catch (ActiveBookingDeletionException e) {
            redirectAttributes.addFlashAttribute("error", "Pågående bokning kan inte tas bort");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ett fel uppstod vid borttagning av bokning.");
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

