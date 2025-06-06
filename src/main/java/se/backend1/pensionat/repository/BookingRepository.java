package se.backend1.pensionat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.backend1.pensionat.entity.Booking;
import se.backend1.pensionat.entity.Room;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE :date BETWEEN b.checkIn AND b.checkOut")
    List<Booking> findBookingsByDate(@Param("date") LocalDate date);

    List<Booking> findByCustomerId(Long customerId);

    //Query för att se om bokningar krockar.
    @Query("""
    SELECT b FROM Booking b 
    WHERE b.room.id = :roomId 
      AND NOT (b.checkOut <= :startDate OR b.checkIn >= :endDate)
""")
    List<Booking> findConflictingBookings(
            @Param("roomId") Long roomId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

}
