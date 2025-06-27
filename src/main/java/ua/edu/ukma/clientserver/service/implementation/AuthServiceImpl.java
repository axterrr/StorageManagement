package ua.edu.ukma.clientserver.service.implementation;

import ua.edu.ukma.clientserver.dao.CredentialsDao;
import ua.edu.ukma.clientserver.dao.DaoFactory;
import ua.edu.ukma.clientserver.exception.AuthException;
import ua.edu.ukma.clientserver.model.Credentials;
import ua.edu.ukma.clientserver.security.JWTUtility;
import ua.edu.ukma.clientserver.security.PasswordHasher;
import ua.edu.ukma.clientserver.service.AuthService;

import java.util.Optional;

public class AuthServiceImpl implements AuthService {

    private final DaoFactory daoFactory;
    private final JWTUtility jwtUtility;
    private final PasswordHasher passwordHasher;

    public AuthServiceImpl(DaoFactory daoFactory, JWTUtility jwtUtility, PasswordHasher passwordHasher) {
        this.daoFactory = daoFactory;
        this.jwtUtility = jwtUtility;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public String authenticate(String username, String password) {
        Optional<Credentials> credentials;
        try (CredentialsDao credentialsDao = daoFactory.credentialsDao()) {
            credentials = credentialsDao.getByUsername(username);
        }
        if (credentials.isEmpty() || !passwordHasher.matches(password, credentials.get().getPassword())) {
            throw new AuthException("Invalid username or password");
        }
        return jwtUtility.createToken(username);
    }
}
