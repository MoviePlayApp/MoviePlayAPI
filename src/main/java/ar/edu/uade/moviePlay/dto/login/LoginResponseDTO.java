package ar.edu.uade.moviePlay.dto.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private Integer userId;
    private String name;
    private String nickname;
    private String email;
    private String imageUri;
    private String token;
    private String refreshToken;
}
