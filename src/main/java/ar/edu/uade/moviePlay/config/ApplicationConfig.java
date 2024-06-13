package ar.edu.uade.moviePlay.config;


import ar.edu.uade.moviePlay.exception.InvalidTokenException;
import ar.edu.uade.moviePlay.repository.IUserRepository;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableAutoConfiguration
public class ApplicationConfig {
    private final IUserRepository repository;

    public ApplicationConfig(IUserRepository repository) {
        this.repository = repository;
    }


    /**
     * Provides an AuthenticationManager bean using the provided AuthenticationConfiguration.
     *
     * @param config The AuthenticationConfiguration to retrieve the AuthenticationManager from.
     * @return The configured AuthenticationManager instance.
     * @throws Exception if an error occurs while configuring the AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Provides an AuthenticationProvider bean using a DaoAuthenticationProvider with a configured UserDetailsService
     * and PasswordEncoder.
     *
     * @return The configured AuthenticationProvider instance.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    /**
     * Provides a PasswordEncoder bean using BCryptPasswordEncoder.
     *
     * @return The configured PasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Provides a UserDetailsService bean using the UserRepository to retrieve UserDetails by username.
     *
     * @return The configured UserDetailsService instance.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return email -> repository.findByEmail(email)
                .orElseThrow(() -> new InvalidTokenException("User not found"));
    }
}
