package se.backend1.pensionat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import se.backend1.pensionat.entity.Room;
import se.backend1.pensionat.model.RoomType;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByRoomType(RoomType roomType);
//    List <Room> findByCapacityGreaterThanEqual(int capacity);


}
