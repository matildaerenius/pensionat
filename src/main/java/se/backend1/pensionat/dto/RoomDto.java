package se.backend1.pensionat.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.backend1.pensionat.model.RoomType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomDto {

    private Long id;

    @NotBlank(message = "Rumsnummer krävs")
    @Size(max = 20, message = "Rumsnummer får max vara 20 tecken")
    private String roomNumber;

    @NotNull(message = "Rumstyp krävs")
    private RoomType roomType;

    private boolean allowExtraBeds;

    @NotNull
    @Min(value = 1, message = "Kapacitet måste vara minst 1")
    @Max(value = 4, message = "Kapacitet får max vara 4")
    private Integer capacity;

    @Min(value = 0, message = "Extrasängar kan inte vara negativt")
    @Max(2)
    private int maxExtraBeds;

}
