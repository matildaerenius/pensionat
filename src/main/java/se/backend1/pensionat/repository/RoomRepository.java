package se.backend1.pensionat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import se.backend1.pensionat.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Integer> {

}
