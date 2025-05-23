package se.backend1.pensionat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.backend1.pensionat.entity.Room;
import se.backend1.pensionat.model.RoomType;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByRoomType(RoomType roomType);

    @Query("SELECT r FROM Room r WHERE (r.capacity + r.maxExtraBeds) >= :minCapacity")
    List<Room> findByTotalCapacityGreaterThanEqual(@Param("minCapacity") int minCapacity);

}
