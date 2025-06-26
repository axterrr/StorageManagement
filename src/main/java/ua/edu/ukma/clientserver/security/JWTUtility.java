package ua.edu.ukma.clientserver.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import ua.edu.ukma.clientserver.config.AppConfig;

import java.util.Date;

public class JWTUtility {

    private static final String SECRET_KEY_PROPERTY = "jwt.secret-key";
    private static final String EXPIRATION_TIME_PROPERTY = "jwt.expiration-time";

    private final Algorithm algorithm;
    private final long expirationTime;

    private JWTUtility() {
        this.algorithm = Algorithm.HMAC512(AppConfig.get(SECRET_KEY_PROPERTY));
        this.expirationTime = AppConfig.getLong(EXPIRATION_TIME_PROPERTY);
    }

    private static class Holder {
        static final JWTUtility INSTANCE = new JWTUtility();
    }

    public static JWTUtility getInstance() {
        return JWTUtility.Holder.INSTANCE;
    }

    public String createToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(algorithm);
    }

    public DecodedJWT verify(String token) {
        return JWT.require(algorithm)
                .build()
                .verify(token);
    }
}
