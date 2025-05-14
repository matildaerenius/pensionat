package se.backend1.pensionat.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.backend1.pensionat.model.RoomType;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "room")

public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoomType type;

    private String roomNumber;

    @Min(value = 1, message = "Minst en gäst krävs")
    @Max(value = 4, message = "Max 4 gäst")
    private int capacity = 4;

    private boolean allowExtraBeds; //VG
//VG
    @Min(value = 0, message = "Minst 0 extrasängar")
    @Max(value = 3, message = "Max 2 extrasängar tillåtna")
    private int maxExtraBeds;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();
}
