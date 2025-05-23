package se.backend1.pensionat.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDto {

    private Long id;

    @NotBlank(message = "Förnamn krävs")
    @Size(max = 30, message = "Namn får vara högst 100 tecken långt")
    private String firstName;

    @NotBlank(message = "Efternamn krävs")
    @Size(max = 30, message = "Namn får vara högst 100 tecken långt")
    private String lastName;

    @NotBlank(message = "E-post krävs")
    @Pattern(regexp = ".*@.*", message = "E-post måste innehålla '@'")
    @Email(message = "Ogiltig e-postadress")
    private String email;

    @NotBlank(message = "Telefonnummer krävs")
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Ogiltigt telefonnummer")
    private String phoneNumber;

    @NotBlank(message = "Adress måste anges")
    @Size(max = 200, message = "Adress får vara högst 200 tecken långt")
    private String address;
}
