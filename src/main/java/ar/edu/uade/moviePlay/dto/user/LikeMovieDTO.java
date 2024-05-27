package ar.edu.uade.moviePlay.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LikeMovieDTO {
    private int movieId;
    private boolean isLiked;
}
