package se.backend1.pensionat.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.backend1.pensionat.dto.BookingDto;
import se.backend1.pensionat.dto.DetailedBookingDto;
import se.backend1.pensionat.entity.Booking;
import se.backend1.pensionat.entity.Customer;
import se.backend1.pensionat.repository.BookingRepository;
import se.backend1.pensionat.service.BookingService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private BookingRepository bookingRepository;


    //Bygger från Booking TILL DTO
    @Override
    public BookingDto bookingToBookingDto (Booking b){
        return BookingDto.builder()
                .id(b.getId())
                .checkIn(b.getCheckIn())
                .checkOut(b.getCheckOut())
                .numberOfGuests(b.getNumberOfGuests())
                .customerId(b.getCustomer().getId())
                .roomId(b.getRoom().getId())
                .build();
    }

    @Override
    public DetailedBookingDto getDetailedBooking(Booking b) {
        return DetailedBookingDto.builder()
                .bookingDto(bookingToBookingDto(b))// bygger på ovanstående
                .customerDto(customerToCustomerDto(b.getCustomer()))
                .roomDto(roomToRoomDto(b.getRoom()))
                .build();
    }





    @Override
    public List<Booking> getBookingsForDate(LocalDate date) {
        return bookingRepository.findBookingsByDate(date);
    }
    @Override
    public boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        List<Booking> conflicts = bookingRepository.findConflictingBookings(roomId, checkIn, checkOut);
        return conflicts.isEmpty(); // true = ledigt, false = dubbelbokning
    }

    @Override
    public void saveBooking(Booking booking) {

    }


    @Override
    public List<Booking> getAllBookings() {
        return List.of();
    }

}
