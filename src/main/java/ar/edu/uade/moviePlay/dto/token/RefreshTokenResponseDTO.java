package ar.edu.uade.moviePlay.dto.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenResponseDTO {
    private String status;
    private String token;
    private String refreshToken;
}
