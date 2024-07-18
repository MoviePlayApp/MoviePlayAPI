package ar.edu.uade.moviePlay.service.Impl;

import ar.edu.uade.moviePlay.dto.movie.MovieDataDTO;
import ar.edu.uade.moviePlay.service.MovieDetailsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class MovieDetailsServiceImpl implements MovieDetailsService {

    private final WebClient webClient;

    @Value("${themoviedb.api.token}")
    private String apiToken;

    @Value("${themoviedb.api.imageBaseUrl}")
    private String imageBaseUrl;

    public MovieDetailsServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.themoviedb.org/3").build();
    }

    public MovieDataDTO getMovieDetails(String movieId) {
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

}
