package ar.edu.uade.moviePlay.controller;

import ar.edu.uade.moviePlay.dto.movie.MovieDTO;
import ar.edu.uade.moviePlay.dto.user.DeleteMeDTO;
import ar.edu.uade.moviePlay.dto.user.MeDTO;
import ar.edu.uade.moviePlay.dto.user.PutMeDTO;
import ar.edu.uade.moviePlay.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final IUserService userService;

    public UserController(IUserService userService){
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<MeDTO> getMe(HttpServletRequest request){
        return new ResponseEntity<>(userService.getMe(request.getHeader("Authorization")), HttpStatus.OK);
    }

    @DeleteMapping("/me")
    public ResponseEntity<DeleteMeDTO> deleteMe(HttpServletRequest request){
        return new ResponseEntity<>(userService.deleteMe(request.getHeader("Authorization")), HttpStatus.OK);
    }

    @PutMapping("/me")
    public ResponseEntity<MeDTO> putMe(HttpServletRequest request, @RequestBody PutMeDTO putMeDTO){
        return new ResponseEntity<>(userService.putMe(request.getHeader("Authorization"), putMeDTO), HttpStatus.OK);
    }

    @GetMapping("/me/likedMovies")
    public ResponseEntity<List<MovieDTO>> getLikedMovies(HttpServletRequest request){
        return new ResponseEntity<>(userService.getLikedMovies(request.getHeader("Authorization")), HttpStatus.OK);
    }
}
