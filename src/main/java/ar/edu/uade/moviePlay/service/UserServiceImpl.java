package ar.edu.uade.moviePlay.service;

import ar.edu.uade.moviePlay.config.JwtService;
import ar.edu.uade.moviePlay.dto.movie.MovieDTO;
import ar.edu.uade.moviePlay.dto.user.DeleteMeDTO;
import ar.edu.uade.moviePlay.dto.user.MeDTO;
import ar.edu.uade.moviePlay.dto.user.PutMeDTO;
import ar.edu.uade.moviePlay.entity.User;
import ar.edu.uade.moviePlay.exception.InvalidTokenException;
import ar.edu.uade.moviePlay.repository.IUserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService{
    JwtService jwtService;
    IUserRepository userRepository;

    public UserServiceImpl(JwtService jwtService, IUserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
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
        userRepository.deleteById(user.get().getId());
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
        return List.of();
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
