package ar.edu.uade.moviePlay.dto.logout;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogoutResponseDTO {
    private String email;
    private String status;
}
