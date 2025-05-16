package se.backend1.pensionat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import se.backend1.pensionat.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

}
