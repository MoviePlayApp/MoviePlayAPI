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
public class MovieDTO {
    private int id;
    private String backdrop_path;
    private String title;
    private double vote_average; // para ordenar por calificacion
    private String release_date;// para ordenar por fecha de publicacion
    private List<String> genre_ids;
}
