package ar.edu.uade.moviePlay.config;

import ar.edu.uade.moviePlay.exception.ApiError;
import ar.edu.uade.moviePlay.exception.InvalidTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiError> handleInvalidTokenException(InvalidTokenException ex) {
        ApiError apiError = new ApiError("Invalid Token", ex.getMessage(), HttpStatus.FORBIDDEN.value());
        return ResponseEntity.status(apiError.getCode()).body(apiError);
    }

    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public ResponseEntity<ApiError> handleUnauthorizedException(Exception ex){
        ApiError apiError = new ApiError("Unauthorized", ex.getMessage(), HttpStatus.UNAUTHORIZED.value());
        return ResponseEntity.status(apiError.getCode()).body(apiError);
    }
}
