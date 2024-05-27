package ar.edu.uade.moviePlay.dto.movie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieDataDTO {
    private int id;
    private String title;
    private String synopsis;
    private String genre;
    private Integer releaseYear;
    private String duration;
    private String imageUri;
    private String trailerUri;
    private String director;
    private List<String> actors;
    private Float rateAverage;
    private boolean isLiked;
}
