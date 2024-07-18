package ar.edu.uade.moviePlay.service.Impl;

import ar.edu.uade.moviePlay.service.RatingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class RatingServiceImpl implements RatingService {

    private static final Logger logger = LoggerFactory.getLogger(RatingServiceImpl.class);

    private final WebClient webClient;

    @Value("${themoviedb.api.token}")
    private String apiToken;


    public RatingServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.themoviedb.org/3").build();
    }


    @Override
    public String updateMovieRating(String movieId, double rating) {
        try {
            logger.info("Updating movie rating for movieId: {}", movieId);
            logger.info("New rating value: {}", rating);

            String jsonBody = String.format("{\"value\": %.1f}", rating);
            logger.info("Request body: {}", jsonBody);

            Mono<String> response = webClient.post()
                    .uri("/movie/{movie_id}/rating", movieId)
                    .header("Authorization", "Bearer " + apiToken)
                    .header("Content-Type", "application/json")
                    .bodyValue(jsonBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .onErrorResume(e -> {
                        e.printStackTrace();
                        return Mono.just("Error updating rating");
                    });

            String apiResponse = response.block();
            logger.info("API response: {}", apiResponse);

            return apiResponse.contains("success") ? "Rating updated successfully" : "Failed to update rating";
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update movie rating", e);
        }
    }
}
