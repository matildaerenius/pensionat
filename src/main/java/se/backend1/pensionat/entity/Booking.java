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

    @NotNull(message = "Minst en gäst krävs")
    @Min(value = 1)
    private Integer numberOfGuests;

    @NotNull(message = "Kund måste väljas")
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @NotNull(message = "Rum måste väljas")
    //at JSONignore, tar bort
    @ManyToOne
    //cascade? Rum ska försvinna från aktuell bokning, fråga sigrun!
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;



}
