package ar.edu.uade.moviePlay.service.Impl;

import ar.edu.uade.moviePlay.config.JwtService;
import ar.edu.uade.moviePlay.dto.movie.MovieDTO;
import ar.edu.uade.moviePlay.dto.user.*;
import ar.edu.uade.moviePlay.entity.Movie;
import ar.edu.uade.moviePlay.entity.User;
import ar.edu.uade.moviePlay.exception.InvalidTokenException;
import ar.edu.uade.moviePlay.repository.IMovieRepository;
import ar.edu.uade.moviePlay.repository.IUserRepository;
import ar.edu.uade.moviePlay.service.IUserService;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ar.edu.uade.moviePlay.entity.Movie.NewMovieDescription;

@Service
public class UserServiceImpl implements IUserService {
    JwtService jwtService;
    IUserRepository userRepository;
    IMovieRepository movieRepository;

    public UserServiceImpl(JwtService jwtService, IUserRepository userRepository, IMovieRepository movieRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    @Override
    public MeDTO getMe(String token) {
        Optional<User> user = getUserFromToken(token);
        validateUser(user);
        return new MeDTO(user.get().getId(), user.get().getName(), user.get().getNickName(),  user.get().getEmail(),  user.get().getProfilePictureUri());
    }

    @Override
    public DeleteMeDTO deleteMe(String token){
        Optional<User> user = getUserFromToken(token);
        validateUser(user);
        userRepository.delete(user.get());
        if (userRepository.findByEmail(user.get().getEmail()).isEmpty()) {
            return new DeleteMeDTO(user.get().getEmail(), "Account Deleted");
        }
        else{
            return new DeleteMeDTO(user.get().getEmail(), "Account Not Deleted");
        }
    }

    @Override
    public MeDTO putMe(String token, PutMeDTO meDTO) {
        Optional<User> user = getUserFromToken(token);
        validateUser(user);
        String name = meDTO.getName() == null ? user.get().getName():meDTO.getName();
        String nickName = meDTO.getNickname() == null ? user.get().getNickName():meDTO.getNickname();
        String imageUri = meDTO.getImageUri() == null ? user.get().getProfilePictureUri():meDTO.getImageUri();
        user.get().setName(name);
        user.get().setNickName(nickName);
        user.get().setProfilePictureUri(imageUri);
        userRepository.save(user.get());
        return new MeDTO(user.get().getId(), user.get().getName(), user.get().getNickName(), user.get().getEmail(),  user.get().getProfilePictureUri());
    }

    @Override
    public List<MovieDTO> getLikedMovies(String token) {
        Optional<User> user = getUserFromToken(token);
        validateUser(user);
        return user.get().getFavoriteMovies().stream()
                .map(movie ->
                        new MovieDTO(
                                movie.getTmdbId(),
                                movie.getBackdropPath(),
                                movie.getTitle(),
                                movie.getRateAverage(),
                                String.valueOf(movie.getReleaseYear())))
                .collect(Collectors.toList());
    }

    public LikeMovieResponseDTO likeMovie(String token, LikeMovieRequestDTO likeMovieRequestDTO) {
        Optional<User> user = getUserFromToken(token);
        validateUser(user);
        if(user.get().getFavoriteMovies().stream().anyMatch(movie -> movie.getTmdbId()==likeMovieRequestDTO.getMovie().getId())){
            if(!likeMovieRequestDTO.isLiked()){
                List<Movie> movieList = user.get().getFavoriteMovies();
                movieList = movieList.stream().filter(movie -> movie.getTmdbId()!=likeMovieRequestDTO.getMovie().getId()).toList();
                user.get().setFavoriteMovies(movieList);
                userRepository.save(user.get());
            }
            return new LikeMovieResponseDTO(likeMovieRequestDTO.getMovie().getId(), likeMovieRequestDTO.isLiked());
        }
        else{
            if(!likeMovieRequestDTO.isLiked()){
                return new LikeMovieResponseDTO(likeMovieRequestDTO.getMovie().getId(), false);
            }
        }
        boolean result = true;
        Optional<Movie> foundMovie = movieRepository.findByTmdbId(likeMovieRequestDTO.getMovie().getId());
        List<Movie> userFavMovies = user.get().getFavoriteMovies();
        if (foundMovie.isEmpty()) {
            Movie newMovie = NewMovieDescription(
                    likeMovieRequestDTO.getMovie().getId(),
                    likeMovieRequestDTO.getMovie().getBackdrop_path(),
                    likeMovieRequestDTO.getMovie().getTitle(),
                    likeMovieRequestDTO.getMovie().getVote_average(),
                    likeMovieRequestDTO.getMovie().getRelease_date()
            );
            movieRepository.save(newMovie);
            userFavMovies.add(newMovie);
        }else{
            userFavMovies.add(foundMovie.get());
        }
        user.get().setFavoriteMovies(userFavMovies);
        try {
            userRepository.save(user.get());
        } catch (Exception e) {
            result = false;
        }
        return new LikeMovieResponseDTO(likeMovieRequestDTO.getMovie().getId(), result);
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
