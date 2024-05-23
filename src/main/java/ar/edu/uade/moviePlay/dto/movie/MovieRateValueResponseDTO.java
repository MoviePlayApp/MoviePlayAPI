package ar.edu.uade.moviePlay.dto.movie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieRateValueResponseDTO {
    private int movieId;
    private int movieRate;
}
