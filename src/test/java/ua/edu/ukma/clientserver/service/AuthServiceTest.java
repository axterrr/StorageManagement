package ua.edu.ukma.clientserver.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.edu.ukma.clientserver.TestUtils;
import ua.edu.ukma.clientserver.dao.CredentialsDao;
import ua.edu.ukma.clientserver.dao.DaoFactory;
import ua.edu.ukma.clientserver.exception.AuthException;
import ua.edu.ukma.clientserver.model.Credentials;
import ua.edu.ukma.clientserver.security.JWTUtility;
import ua.edu.ukma.clientserver.security.PasswordHasher;
import ua.edu.ukma.clientserver.service.implementation.AuthServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    private AuthService authService;

    @Mock
    private JWTUtility jwtUtility;

    @Mock
    private DaoFactory daoFactory;

    @Mock
    private CredentialsDao credentialsDao;

    @Mock
    private PasswordHasher passwordHasher;

    @BeforeEach
    void beforeEach() {
        authService = new AuthServiceImpl(daoFactory, jwtUtility, passwordHasher);
        when(daoFactory.credentialsDao()).thenReturn(credentialsDao);
    }

    @Test
    void shouldAuthenticate() {
        Credentials credentials = TestUtils.randomCredentials();

        when(credentialsDao.getByUsername(credentials.getUsername())).thenReturn(Optional.of(credentials));
        when(passwordHasher.matches(credentials.getPassword(), credentials.getPassword())).thenReturn(true);
        when(jwtUtility.createToken(credentials.getUsername())).thenReturn("token");

        String token = authService.authenticate(credentials.getUsername(), credentials.getPassword());
        assertEquals("token", token);
    }

    @Test
    void shouldFailForUnknownUsername() {
        Credentials credentials = TestUtils.randomCredentials();

        when(credentialsDao.getByUsername("incorrect")).thenReturn(Optional.empty());

        assertThrows(AuthException.class, () -> authService.authenticate("incorrect", credentials.getPassword()));
    }

    @Test
    void shouldFailForIncorrectPassword() {
        Credentials credentials = TestUtils.randomCredentials();

        when(credentialsDao.getByUsername(credentials.getUsername())).thenReturn(Optional.of(credentials));
        when(passwordHasher.matches("incorrect", credentials.getPassword())).thenReturn(false);

        assertThrows(AuthException.class, () -> authService.authenticate(credentials.getUsername(), "incorrect"));
    }
}
