package se.backend1.pensionat;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import se.backend1.pensionat.entity.Room;
import se.backend1.pensionat.model.RoomType;
import se.backend1.pensionat.repository.RoomRepository;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class PensionatApplication {

    public static void main(String[] args) {
        SpringApplication.run(PensionatApplication.class, args);
    }


    @Bean
    public CommandLineRunner commandLineRunner(RoomRepository repository) {
        return args -> {
            if (repository.count() == 0) {

                List<Room> rooms = new ArrayList<>();

                rooms.add(createRoom("101", RoomType.SINGLE, 1, false, 0));
                rooms.add(createRoom("102", RoomType.SINGLE, 1, false, 0));
                rooms.add(createRoom("103", RoomType.DOUBLE, 2, true, 1));
                rooms.add(createRoom("104", RoomType.DOUBLE, 2, true, 2));
                rooms.add(createRoom("105", RoomType.DOUBLE, 3, true, 1));
                rooms.add(createRoom("106", RoomType.DOUBLE, 4, true, 2));
                rooms.add(createRoom("107", RoomType.DOUBLE, 3, true, 1));
                rooms.add(createRoom("108", RoomType.DOUBLE, 4, true, 2));
                rooms.add(createRoom("109", RoomType.DOUBLE, 3, true, 1));
                rooms.add(createRoom("110", RoomType.DOUBLE, 4, true, 2)); // Max tillåtet extrasängar

                repository.saveAll(rooms);
            }
        };
    }

    private Room createRoom(String number, RoomType type, int capacity, boolean allowExtraBeds, int maxExtraBeds) {
        Room room = new Room();
        room.setRoomNumber(number);
        room.setRoomType(type);
        room.setCapacity(capacity);
        room.setAllowExtraBeds(allowExtraBeds);
        room.setMaxExtraBeds(maxExtraBeds);
        return room;
    }


}
