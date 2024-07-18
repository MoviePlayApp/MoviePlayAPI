package ar.edu.uade.moviePlay.service.Impl;

import ar.edu.uade.moviePlay.config.JwtService;
import ar.edu.uade.moviePlay.dto.movie.MovieDataDTO;
import ar.edu.uade.moviePlay.entity.User;
import ar.edu.uade.moviePlay.exception.InvalidTokenException;
import ar.edu.uade.moviePlay.repository.IMovieRepository;
import ar.edu.uade.moviePlay.repository.IUserRepository;
import ar.edu.uade.moviePlay.service.MovieDetailsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class MovieDetailsServiceImpl implements MovieDetailsService {

    private final WebClient webClient;

    @Value("${themoviedb.api.token}")
    private String apiToken;

    @Value("${themoviedb.api.imageBaseUrl}")
    private String imageBaseUrl;

    private IUserRepository userRepository;
    private JwtService jwtService;


    public MovieDetailsServiceImpl(WebClient.Builder webClientBuilder, IUserRepository userRepository, JwtService jwtService, IMovieRepository movieRepository) {
        this.webClient = webClientBuilder.baseUrl("https://api.themoviedb.org/3").build();
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public MovieDataDTO getMovieDetails(String movieId, String token) {
        Mono<String> response = webClient.get()
                .uri("/movie/{movie_id}", movieId)
                .header("Authorization", "Bearer " + apiToken)
                .retrieve()
                .bodyToMono(String.class);

        String movieResponse = response.block();

        MovieDataDTO movieDataDTO = null;
        if (movieResponse != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode root = objectMapper.readTree(movieResponse);
                Optional<User> user = getUserFromToken(token);
                validateUser(user);
                boolean isLikedByUser = user.get().getFavoriteMovies().stream().anyMatch(movie -> String.valueOf(movie.getTmdbId()).equals(movieId));
                movieDataDTO = MovieDataDTO.builder()
                        .title(root.path("title").asText())
                        .tagline(root.path("tagline").asText())
                        .overview(root.path("overview").asText())
                        .poster_path(imageBaseUrl + "original" + root.path("poster_path").asText())
                        .backdrop_path(imageBaseUrl + "original" + root.path("backdrop_path").asText())
                        .release_date(root.path("release_date").asText())
                        .runtime(root.path("runtime").asInt())
                        .vote_average(root.path("vote_average").asDouble())
                        .vote_count(root.path("vote_count").asInt())
                        .isLiked(isLikedByUser)
                        .build();

                List<String> genres = new ArrayList<>();
                for (JsonNode genreNode : root.path("genres")) {
                    genres.add(genreNode.path("name").asText());
                }
                movieDataDTO.setGenres(genres);

                // director and main actors
                Mono<String> creditsResponse = webClient.get()
                        .uri("/movie/{movie_id}/credits", movieId)
                        .header("Authorization", "Bearer " + apiToken)
                        .retrieve()
                        .bodyToMono(String.class);

                String credits = creditsResponse.block();

                if (credits != null) {
                    JsonNode creditsRoot = objectMapper.readTree(credits);

                    // director
                    JsonNode crew = creditsRoot.path("crew");
                    for (Iterator<JsonNode> it = crew.elements(); it.hasNext(); ) {
                        JsonNode node = it.next();
                        if ("Director".equalsIgnoreCase(node.path("job").asText())) {
                            movieDataDTO.setDirector(node.path("name").asText());
                            break;
                        }
                    }

                    // main actors (limit to 10)
                    List<String> mainActors = new ArrayList<>();
                    JsonNode cast = creditsRoot.path("cast");
                    int count = 0;
                    for (Iterator<JsonNode> it = cast.elements(); it.hasNext() && count < 10; ) {
                        JsonNode node = it.next();
                        mainActors.add(node.path("name").asText());
                        count++;
                    }
                    movieDataDTO.setMainActors(mainActors);
                }

                // Obtener el tráiler
                Mono<String> videoResponse = webClient.get()
                        .uri("/movie/{movie_id}/videos", movieId)
                        .header("Authorization", "Bearer " + apiToken)
                        .retrieve()
                        .bodyToMono(String.class);

                String videos = videoResponse.block();
                if (videos != null) {
                    JsonNode videosRoot = objectMapper.readTree(videos);
                    JsonNode results = videosRoot.path("results");
                    for (JsonNode videoNode : results) {
                        if ("Trailer".equalsIgnoreCase(videoNode.path("type").asText()) &&
                                "YouTube".equalsIgnoreCase(videoNode.path("site").asText())) {
                            movieDataDTO.setTrailerUri(videoNode.path("key").asText());
                            break;
                        }
                    }
                }

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return movieDataDTO;
    }

    private void validateTokenFormat(String token){
        if (!StringUtils.hasText(token) || !token.startsWith("Bearer ")) {
            throw new InvalidTokenException("Invalid token");
        }
    }

    private Optional<User> getUserFromToken(String token){
        validateTokenFormat(token);
        String email = jwtService.getClaim(token.substring(7), Claims::getSubject);
        return userRepository.findByEmail(email);
    }

    private void validateUser(Optional<User> user){
        if(user.isEmpty()){
            throw new InvalidTokenException("Invalid token");
        }
    }
}
