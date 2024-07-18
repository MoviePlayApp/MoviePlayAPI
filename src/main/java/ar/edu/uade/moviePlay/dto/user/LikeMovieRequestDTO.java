package ar.edu.uade.moviePlay.dto.user;

import ar.edu.uade.moviePlay.dto.movie.MovieDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LikeMovieRequestDTO {
    @JsonProperty(value = "movie", required = true)
    private MovieDTO movie;
    @JsonProperty(value = "isLiked", required = true)
    private boolean isLiked;
}
