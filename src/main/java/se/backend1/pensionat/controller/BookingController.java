package se.backend1.pensionat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.backend1.pensionat.repository.BookingRepository;
import se.backend1.pensionat.service.BookingService;
import se.backend1.pensionat.service.CustomerService;
import se.backend1.pensionat.service.RoomService;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    private BookingRepository repo;

    public BookingController(BookingRepository repository) {
        this.repo = repository;
    }


    private  BookingService bookingService;

   // private RoomService roomService;

    @Autowired
    public BookingController(BookingService ser) {this.bookingService = ser; }


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
