package se.backend1.pensionat.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Incheckningsdatum krävs")
    private LocalDate checkIn;

    @NotNull(message = "Utcheckningsdatum krävs")
    private LocalDate checkOut;

    @Min(value = 1, message = "Minst en gäst krävs")
    private Integer numberOfGuests;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    //at JSONignore, tar bort
    @ManyToOne
    //cascade? Rum ska försvinna från aktuell bokning, fråga sigrun!
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;


}
