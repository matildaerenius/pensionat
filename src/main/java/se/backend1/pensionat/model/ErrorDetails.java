package se.backend1.pensionat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetails {

    private String title;
    private String message;
    private LocalDateTime timestamp;

    public ErrorDetails(String title, String message) {
        this.title = title;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
