package se.backend1.pensionat.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import se.backend1.pensionat.entity.Room;
import se.backend1.pensionat.model.RoomType;
import se.backend1.pensionat.repository.RoomRepository;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final RoomRepository roomRepository;

    @PostConstruct
    public void initData() {
        if (roomRepository.count() == 0) {
            Room r1 = Room.builder()
                    .roomNumber("101")
                    .roomType(RoomType.SINGLE)
                    .capacity(1)
                    .allowExtraBeds(false)
                    .maxExtraBeds(0)
                    .build();

            Room r2 = Room.builder()
                    .roomNumber("102")
                    .roomType(RoomType.DOUBLE)
                    .capacity(2)
                    .allowExtraBeds(true)
                    .maxExtraBeds(1)
                    .build();

            Room r3 = Room.builder()
                    .roomNumber("201")
                    .roomType(RoomType.DOUBLE)
                    .capacity(4)
                    .allowExtraBeds(true)
                    .maxExtraBeds(2)
                    .build();

            roomRepository.save(r1);
            roomRepository.save(r2);
            roomRepository.save(r3);

            System.out.println(">>> Rum skapade och sparade i databasen.");
        } else {
            System.out.println(">>> Rum finns redan, skippar initiering.");
        }
    }
}
