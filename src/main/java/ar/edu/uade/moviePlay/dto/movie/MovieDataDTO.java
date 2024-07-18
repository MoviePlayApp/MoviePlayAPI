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
    private String tagline;
    private String overview;
    private List<String> genres;
    private String backdrop_path;
    private String poster_path;
    private String release_date;
    private int runtime;
    private String trailerUri;
    private String director;
    private List<String> mainActors;
    private double vote_average;
    private int vote_count;
    private boolean isLiked;
}
