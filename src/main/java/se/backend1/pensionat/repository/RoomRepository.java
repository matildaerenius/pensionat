package se.backend1.pensionat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import se.backend1.pensionat.entity.Booking;
import se.backend1.pensionat.entity.Room;
import se.backend1.pensionat.model.RoomType;

import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByRoomType(RoomType roomType);

    //!!!!! Bör det verkligen returnera room??
    //vill vi ta ut bokningar eller lediga rum?
    //Ta ut bokningar för aktuellt datum för att se vilka som ska ha bokningar
//    @Query("SELECT b FROM Booking b WHERE b.checkIn < :checkOut AND b.checkOut > :checkIn")
//    List<Room> findBookingsBetweenDates(
//            @Param("checkIn") LocalDate checkIn,
//            @Param("checkOut") LocalDate checkOut
//    );


    @Query("SELECT r FROM Room r WHERE (r.capacity + r.maxExtraBeds) >= :minCapacity")
    List<Room> findByTotalCapacityGreaterThanEqual(@Param("minCapacity") int minCapacity);

}
