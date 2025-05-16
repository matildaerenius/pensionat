package se.backend1.pensionat.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import se.backend1.pensionat.dto.BookingDto;
import se.backend1.pensionat.entity.Booking;
import se.backend1.pensionat.entity.Room;
import se.backend1.pensionat.repository.BookingRepository;
import se.backend1.pensionat.service.BookingService;
import se.backend1.pensionat.service.CustomerService;
import se.backend1.pensionat.service.RoomService;

import java.util.List;

@RestController
@RequiredArgsConstructor //detta gör att vi kan ta bort Autowired o slipper göra konstruktorer
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final CustomerService customerService;
    private final RoomService roomService;


    @GetMapping()
    public String getAllBookings(Model model){
        List<Booking> bookings = bookingService.getAllBookings();
        model.addAttribute("bookings", bookings);
        return "bookings/list";
    }

    /**
     * Visar formulär för att skapa en ny bokning
     */
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("bookingDto", new BookingDto());
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("rooms", roomService.getAllRooms());
        return "bookings/create";
    }

    @PostMapping("/create")
    public String createBooking(@ModelAttribute("bookingDto") @Valid BookingDto bookingDto,
                                BindingResult result, RedirectAttributes redirectAttributes, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("customers", customerService.getAllCustomers());
            model.addAttribute("rooms", roomService.getAllRooms());
            return "bookings/create";
        }

        if (!bookingService.isRoomAvailable(bookingDto.getRoomId(), bookingDto.getCheckIn(), bookingDto.getCheckOut())) {
            result.rejectValue("roomId", "error.bookingDto", "Rummet är redan bokat för valt datum.");
            model.addAttribute("customers", customerService.getAllCustomers());
            model.addAttribute("rooms", roomService.getAllRooms());
            return "bookings/create";
        }

        Booking booking = BookingMapper.toEntity(bookingDto);
        booking.setCustomer(customerService.getCustomerById(bookingDto.getCustomerId())); // behöver toEntity om DTO returneras
        booking.setRoom(roomService.getRoomById(bookingDto.getRoomId())); // samma här

        bookingService.saveBooking(booking);
        redirectAttributes.addFlashAttribute("success", "Bokningen skapades!");
        return "redirect:/bookings";
    }

    @PostMapping("/edit/{id}")
    public String editBooking(@PathVariable Long id){
        return null;
    }

    @PostMapping("/delete/{id}")
    public String deleteBooking(@PathVariable Long id){
        bookingService.deleteBooking(id);
        return "redirect:/bookings";
    }

    // @PatchMapping Denna uppdaterar bara en del?
    //annars är det samma metod som editbooking där uppe
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

        if (!bookingService.isRoomAvailable(bookingDto.getRoomId(), bookingDto.getCheckIn(), bookingDto.getCheckOut())) {
            result.rejectValue("roomId", "error.bookingDto", "Rummet är inte ledigt under den vald tidsinterbvall.");
            model.addAttribute("customers", customerService.getAllCustomers());
            model.addAttribute("rooms", roomService.getAllRooms());
            return "bookings/edit";
        }

        bookingService.updateBooking(id, bookingDto);
        return "redirect:/bookings";
    }

    @GetMapping("/search")
    public String showSearchForm() {
        return "bookings/search";
    }

    //tar ut rum med bokning? Eller ska vi ta ut kund?
    // Står på trello att vi ska ta ut sökformulär
    // Hämtar bokningar för ett rum
    @GetMapping("/bookings/search")
    public String getBookingsByRoom(@RequestParam Long roomId, Model model){
        List<Booking> bookings = bookingService.getBookingsByRoomId(roomId);
        model.addAttribute("bookings", bookings);
        return "bookings/list";
    }
    //Vi ska få ut lediga rum baserat på datum och antal personer
    @GetMapping("/bookings/search-results")
    public String getRoomsAvailable(@RequestParam String checkIn,
                                    @RequestParam String checkOut,
                                    @RequestParam int guests,
                                    Model model) {
        List<Room> availableRooms = bookingService.findAvailableRooms(checkIn, checkOut, guests);
        model.addAttribute("availableRooms", availableRooms);
        return "bookings/available-rooms";
    }

    /**
     * Visar formulär för att redigera bokning
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Booking booking = bookingService.getBookingById(id);
        BookingDto bookingDto = BookingMapper.toDto(booking);
        model.addAttribute("bookingDto", bookingDto);
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("rooms", roomService.getAllRooms());
        return "bookings/edit";
    }

}


/**
 * Kustomiserade Querys
 *
 * två olika sätt att skriva
 *
 *  * @Modifying
 *  * @Transactional
 *  * @Query ("update X set y=:newVal where x=:oldVal")
 *  public void updateVal(Sting newVal, String oldVal)
 *
 * @Modifying
 * @Transactional
 * @Query ("update X set y=?1 where x=?2")
 * public void updateVal(@Param ("newVal")
 * String newVal, @param("oldvVal) String oldVal)
 *
 */
