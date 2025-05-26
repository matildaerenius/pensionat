package se.backend1.pensionat.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.backend1.pensionat.model.RoomType;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Rumstyp krävs")
    private RoomType roomType;

    @NotBlank(message = "Rumsnummer krävs")
    @Size(max = 20, message = "Rumsnummer får max vara 20 tecken")
    private String roomNumber;

    @NotNull(message = "Kapacitet krävs")
    @Min(value = 1, message = "Minst en gäst krävs")
    @Max(value = 4, message = "Max 4 gäster")
    @Column(nullable = false)
    private Integer capacity;

    private boolean allowExtraBeds;

    @Min(value = 0, message = "Minst 0 extrasängar")
    @Max(value = 2, message = "Max 2 extrasängar tillåtna")
    @Column(nullable = false)
    private int maxExtraBeds;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();
    
}
