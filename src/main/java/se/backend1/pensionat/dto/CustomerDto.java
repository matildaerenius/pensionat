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

    @NotBlank(message = "Namn får inte vara tomt")
    @Size(max = 30, message = "Namn får vara högst 100 tecken långt")
    private String firstName;

    @NotBlank(message = "Namn får inte vara tomt")
    @Size(max = 30, message = "Namn får vara högst 100 tecken långt")
    private String lastName;

    @NotBlank(message = "E-post får inte vara tom")
    @Pattern(regexp = ".*@.*", message = "Adress måste innehålla tecknet '@'")
    @Email(message = "Ogiltig e-postadress")
    private String email;

    @NotBlank(message = "Telefonnummer får inte vara tomt")
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Ogiltigt telefonnummer")
    private String phoneNumber;

    @NotBlank(message = "Adress får inte vara tom")
    @Size(max = 200, message = "Adress får vara högst 200 tecken lång")
    private String address;
}
