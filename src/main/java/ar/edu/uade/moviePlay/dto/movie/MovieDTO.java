package ar.edu.uade.moviePlay.dto.movie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieDTO {
    private int id;
    private String backdrop_path;
    private String title;
    private double vote_average;
    private String release_date; // String para recibir de la API
}
