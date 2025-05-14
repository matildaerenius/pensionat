package se.backend1.pensionat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.backend1.pensionat.repository.BookingRepository;
import se.backend1.pensionat.service.BookingService;

@RestController
@RequestMapping("/api/booking")
public class BookingController {
    private final BookingRepository repo;

    public BookingController(BookingRepository repository) {
        this.repo = repository;
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
