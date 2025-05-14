package se.backend1.pensionat.service;

import se.backend1.pensionat.entity.Booking;

import java.time.LocalDate;
import java.util.List;

public interface BookingService  {
    List<Booking> getBookingsForDate(LocalDate date);

}
