package ua.edu.ukma.clientserver.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;
import ua.edu.ukma.clientserver.dao.CredentialsDao;
import ua.edu.ukma.clientserver.dao.DaoFactory;

public class JWTAuthenticator extends Authenticator {

    private final DaoFactory daoFactory;
    private final JWTUtility jwtUtility;;

    public JWTAuthenticator(DaoFactory daoFactory, JWTUtility jwtUtility) {
        this.jwtUtility = jwtUtility;
        this.daoFactory = daoFactory;
    }

    @Override
    public Result authenticate(HttpExchange exchange) {
        String authorization = exchange.getRequestHeaders().getFirst("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return new Failure(401);
        }

        String token = authorization.replaceFirst("Bearer ", "");
        String username;
        try {
            username = jwtUtility.verify(token).getSubject();
        } catch (JWTVerificationException e) {
            return new Failure(401);
        }

        try (CredentialsDao credentialsDao = daoFactory.credentialsDao()) {
            if (credentialsDao.getByUsername(username).isEmpty()) {
                return new Failure(401);
            }
        }

        return new Success(new HttpPrincipal(username, "user"));
    }
}
