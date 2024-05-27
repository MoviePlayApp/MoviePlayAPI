package ar.edu.uade.moviePlay.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeDTO {
    private int userId;
    private String name;
    private String nickname;
    private String email;
    private String imageUri;
}
