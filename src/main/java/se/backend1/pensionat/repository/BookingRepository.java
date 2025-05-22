package se.backend1.pensionat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.backend1.pensionat.entity.Booking;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE :date BETWEEN b.checkIn AND b.checkOut")
    List<Booking> findBookingsByDate(@Param("date") LocalDate date);

    //Oklart om dessa ska vara med
    List<Booking> findByCustomerId(Long customerId); // Alla bokningar för kund
    //GÖR DENNA

    //Query för att se om bokningar krockar.
    @Query("""
    SELECT b FROM Booking b 
    WHERE b.room.id = :roomId 
      AND NOT (b.checkOut <= :startDate OR b.checkIn >= :endDate)
""")
    List<Booking> findConflictingBookings(
            @Param("roomId") Long roomId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut
    );

}
