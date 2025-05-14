package se.backend1.pensionat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import se.backend1.pensionat.entity.Booking;
import se.backend1.pensionat.entity.Room;
import se.backend1.pensionat.repository.BookingRepository;
import se.backend1.pensionat.service.BookingService;
import se.backend1.pensionat.service.CustomerService;
import se.backend1.pensionat.service.RoomService;

import java.util.List;

@Controller
@RequestMapping("/api/booking")
public class BookingController {

    private BookingService bookingService;
    private CustomerService customerService;
    private RoomService roomService;
    @Autowired
    public BookingController(BookingService bookingService, CustomerService customerService, RoomService roomService) {
        this.bookingService = bookingService;
        this.customerService = customerService;
        this.roomService = roomService;
    }

    @GetMapping("/bookings")
    public List<Booking> getAllBookings(){
        return null;
    }

    @PostMapping("/bookings/create")
    public String createBooking(){
        return null;
        //meddelande att det är klart kanske?
    }

    @PutMapping("/bookings/edit/{id}")
    public String editBooking(@PathVariable Long id){
        return null;
    }

    @DeleteMapping("/bookings/delete/{id}")
    public String deleteBooking(@PathVariable Long id){
        return null;
    }
    @PostMapping("/booking")
    public String addBooking(Booking booking){
        return null;
    }

    // @PatchMapping Denna uppdaterar bara en del?
    //annars är det samma metod som editbooking där uppe
    @PutMapping("/bookings/update/{id}")
    public String updateBooking(@PathVariable Long id){
        return null;
    }
    //tar ut rum med bokning? Eller ska vi ta ut kund?
    // Står på trello att vi ska ta ut sökformulär
    @GetMapping("/bookings/search")
    public List<Booking> getBookingsByRoom(@RequestParam Long roomId){
        return null;
    }
    //Vi ska få ut lediga rum baserat på datum och antal personer
    @GetMapping("/bookings/search-results")
    public List<Room> getRoomsAvailable(){return null;}


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
