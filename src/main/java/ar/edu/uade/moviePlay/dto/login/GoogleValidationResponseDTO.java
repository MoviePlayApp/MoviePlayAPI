package ar.edu.uade.moviePlay.dto.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoogleValidationResponseDTO {
    private String name;
    private String email;
    private String imageUri;
}
