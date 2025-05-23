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
import se.backend1.pensionat.exception.ActiveBookingDeletionException;
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
        List<DetailedBookingDto> detailedBookings = bookingService.getAllDetailedBookings();
        model.addAttribute("bookings", detailedBookings);
        return "bookings/list";
    }

    @GetMapping("/create")
    public String showCreateForm(@RequestParam(required = false) String checkIn,
                                 @RequestParam(required = false) String checkOut,
                                 @RequestParam(required = false) Integer guests,
                                 @RequestParam(required = false) Long roomId,
                                 Model model) {

        BookingDto bookingDto = new BookingDto();

        if (checkIn != null && !checkIn.isBlank()) {
            bookingDto.setCheckIn(LocalDate.parse(checkIn));
        }

        if (checkOut != null && !checkOut.isBlank()) {
            bookingDto.setCheckOut(LocalDate.parse(checkOut));
        }

        if (guests != null) {
            bookingDto.setNumberOfGuests(guests);
        }

        if (roomId != null) {
            bookingDto.setRoomId(roomId);
        }

        model.addAttribute("bookingDto", bookingDto);
        populateFormDependencies(model, bookingDto, guests);
        model.addAttribute("edit", false);
        model.addAttribute("formAction", "/bookings/create");

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


        RoomDto room = roomService.getRoomById(bookingDto.getRoomId());
        int maxGuests = room.getCapacity() + (room.isAllowExtraBeds() ? room.getMaxExtraBeds() : 0);

        if (bookingDto.getNumberOfGuests() > maxGuests) {
            result.rejectValue("numberOfGuests", "invalid", "För många gäster för detta rum. Max: " + maxGuests);
        }

        if (result.hasErrors()) {
            model.addAttribute("bookingDto", bookingDto);
            populateFormDependencies(model, bookingDto, bookingDto.getNumberOfGuests());
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
            populateFormDependencies(model, bookingDto, bookingDto.getNumberOfGuests());
            model.addAttribute("edit", false);
            model.addAttribute("formAction", "/bookings/create");
            return "bookings/form";
        }
    }


    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id,
                               @RequestParam(required = false) String checkIn,
                               @RequestParam(required = false) String checkOut,
                               @RequestParam(required = false) Integer guests,
                               @RequestParam(required = false) Long roomId,
                               Model model) {

        BookingDto bookingDto = bookingService.getBookingById(id);

        if (checkIn != null && !checkIn.isBlank()) {
            bookingDto.setCheckIn(LocalDate.parse(checkIn));
        }

        if (checkOut != null && !checkOut.isBlank()) {
            bookingDto.setCheckOut(LocalDate.parse(checkOut));
        }

        if (guests != null) {
            bookingDto.setNumberOfGuests(guests);
        }

        if (roomId != null) {
            bookingDto.setRoomId(roomId);
        }

        model.addAttribute("bookingDto", bookingDto);
        populateFormDependencies(model, bookingDto, bookingDto.getNumberOfGuests());
        model.addAttribute("edit", true);
        model.addAttribute("formAction", "/bookings/edit/" + id);

        return "bookings/form";
    }


    @PostMapping("/edit/{id}")
    public String updateBooking(@PathVariable Long id,
                                @ModelAttribute("bookingDto") @Valid BookingDto bookingDto,
                                RedirectAttributes redirectAttributes,
                                BindingResult result,
                                Model model) {
        RoomDto room = roomService.getRoomById(bookingDto.getRoomId());
        int maxGuests = room.getCapacity() + (room.isAllowExtraBeds() ? room.getMaxExtraBeds() : 0);

        if (bookingDto.getNumberOfGuests() > maxGuests) {
            result.rejectValue("numberOfGuests", "invalid", "För många gäster för detta rum. Max: " + maxGuests);
        }

        if (result.hasErrors()) {
            model.addAttribute("bookingDto", bookingDto);
            model.addAttribute("customers", customerService.getAllCustomers());

            List<RoomDto> rooms = roomService.findAvailableRoomFromQuery(
                    bookingDto.getCheckIn(), bookingDto.getCheckOut(), bookingDto.getNumberOfGuests());

            if (bookingDto.getRoomId() != null &&
                    rooms.stream().noneMatch(r -> r.getId().equals(bookingDto.getRoomId()))) {
                RoomDto currentRoom = roomService.getRoomById(bookingDto.getRoomId());
                rooms.add(0, currentRoom);
            }

            model.addAttribute("rooms", rooms);
            model.addAttribute("edit", true);
            model.addAttribute("formAction", "/bookings/edit/" + id);
            return "bookings/form";
        }


        bookingService.updateBooking(id, bookingDto);
        redirectAttributes.addFlashAttribute("success", "Bokning uppdaterad!");
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

    // Hjälpmetod :)
    private void populateFormDependencies(Model model, BookingDto bookingDto, Integer guests) {
        model.addAttribute("customers", customerService.getAllCustomers());

        List<RoomDto> rooms;

        if (bookingDto.getCheckIn() != null && bookingDto.getCheckOut() != null && guests != null) {
            rooms = roomService.findAvailableRoomFromQuery(bookingDto.getCheckIn(), bookingDto.getCheckOut(), guests);

            if (bookingDto.getRoomId() != null &&
                    rooms.stream().noneMatch(r -> r.getId().equals(bookingDto.getRoomId()))) {
                RoomDto currentRoom = roomService.getRoomById(bookingDto.getRoomId());
                rooms.add(0, currentRoom);
            }
        } else {
            rooms = roomService.getAllRooms();
        }

        model.addAttribute("rooms", rooms);
    }


}

