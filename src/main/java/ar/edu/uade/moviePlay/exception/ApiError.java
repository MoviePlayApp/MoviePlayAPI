package ar.edu.uade.moviePlay.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiError {
    private String error;
    private String message;
    private int code;
}
