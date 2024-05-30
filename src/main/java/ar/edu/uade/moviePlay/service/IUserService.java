package ar.edu.uade.moviePlay.service;

import ar.edu.uade.moviePlay.dto.movie.MovieDTO;
import ar.edu.uade.moviePlay.dto.user.DeleteMeDTO;
import ar.edu.uade.moviePlay.dto.user.MeDTO;
import ar.edu.uade.moviePlay.dto.user.PutMeDTO;

import java.util.List;

public interface IUserService {
    MeDTO getMe(String token);
    DeleteMeDTO deleteMe(String token);
    MeDTO putMe(String token, PutMeDTO meDTO);
    List<MovieDTO> getLikedMovies(String token);
}
