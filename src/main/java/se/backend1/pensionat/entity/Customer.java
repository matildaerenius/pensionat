package se.backend1.pensionat.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Förnamn krävs")
    @Size(max = 30, message = "Namn får max vara 100 tecken")
    private String firstName;

    @NotBlank(message = "Efternamn krävs")
    @Size(max = 100, message = "Namn får max vara 100 tecken")
    private String lastName;

    @NotBlank(message = "E-post krävs")
    @Email(message = "Ogiltig e-postadress")
    @Size(max = 100)
    private String email;

    @NotBlank(message = "Telefonnummer krävs")
    @Size(max = 20)
    private String phoneNumber;

    @NotBlank(message = "Adress måste anges")
    @Size(max = 200)
    private String address;

    @OneToMany(mappedBy = "customer")
    private List<Booking> bookings;

}
