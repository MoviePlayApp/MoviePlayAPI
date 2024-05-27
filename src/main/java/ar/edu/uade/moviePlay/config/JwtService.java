package ar.edu.uade.moviePlay.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${secret.jwt.key}")
    private String SECRET_JWT_KEY;

    public String getToken(UserDetails user) {
        return getToken(new HashMap<>(), user);
    }

    /**
     * Generates a JWT token with the provided extra claims and user details.
     *
     * @param extraClaims Additional claims to be included in the JWT token.
     * @param user The UserDetails object representing the user.
     * @return The JWT token string.
     */
    private String getToken(Map<String, Object> extraClaims, UserDetails user) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Retrieves the secret key used for signing JWT tokens.
     *
     * @return The Key object representing the secret key.
     */
    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_JWT_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extracts the username from the provided JWT token.
     *
     * @param token The JWT token string.
     * @return The username extracted from the token.
     */
    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    /**
     * Validates whether the provided JWT token is valid for the given UserDetails.
     *
     * @param token The JWT token string.
     * @param userDetails The UserDetails object representing the user.
     * @return True if the token is valid for the user, false otherwise.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Retrieves all claims from the provided JWT token.
     *
     * @param token The JWT token string.
     * @return The Claims object containing all claims from the token.
     */
    private Claims getAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(SECRET_JWT_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Retrieves a specific claim from the provided JWT token using the given claims resolver function.
     *
     * @param <T> The type of the claim to be retrieved.
     * @param token The JWT token string.
     * @param claimsResolver The function to resolve the specific claim from the token's claims.
     * @return The resolved claim.
     */
    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Retrieves the expiration date of the provided JWT token.
     *
     * @param token The JWT token string.
     * @return The expiration date of the token.
     */
    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    /**
     * Checks whether the provided JWT token has expired.
     *
     * @param token The JWT token string.
     * @return True if the token has expired, false otherwise.
     */
    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }
}