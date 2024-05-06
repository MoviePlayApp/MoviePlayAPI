package ar.edu.uade.moviePlay.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="movie")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="movie_title")
    private String title;

    @ManyToOne
    @JoinColumn(name = "genre")
    private Genre genre;

    @Column(name="movie_release_year")
    private int releaseYear;

    @Column(name="movie_director")
    private String director;

    @Column(name="movie_actors")
    private String actors;

    @Column(name = "movie_length")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime length;

    @Column(name = "movie_synopsis")
    private String synopsis;

    @OneToMany
    @JoinColumn(name="rate")
    private List<MovieRate> rates;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name= "images", joinColumns = @JoinColumn(name = "movie_id"))
    @Column(name = "image", nullable = false)
    private List<String> images;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name= "trailers", joinColumns = @JoinColumn(name = "movie_id"))
    @Column(name = "trailer", nullable = false)
    private List<String> trailers;

    @ManyToMany(mappedBy = "favoriteMovies", fetch = FetchType.EAGER)
    private List<User> users;
}
