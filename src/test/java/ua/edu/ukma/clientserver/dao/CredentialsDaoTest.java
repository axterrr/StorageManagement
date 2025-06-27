package ua.edu.ukma.clientserver.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.edu.ukma.clientserver.TestUtils;
import ua.edu.ukma.clientserver.dao.jdbc.JdbcCredentialsDao;
import ua.edu.ukma.clientserver.model.Credentials;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CredentialsDaoTest extends BaseDaoTest {

    private CredentialsDao credentialsDao;

    @BeforeEach
    void setUp() {
        credentialsDao = new JdbcCredentialsDao(connection);
    }

    @Test
    void testGetByUsername() throws SQLException {
        Credentials credentials = TestUtils.randomCredentials();
        try (PreparedStatement query = connection.prepareStatement("INSERT INTO credentials (username, password) VALUES (?, ?)")) {
            query.setString(1, credentials.getUsername());
            query.setString(2, credentials.getPassword());
            query.executeUpdate();
        }
        Credentials result = credentialsDao.getByUsername(credentials.getUsername()).get();
        assertEquals(credentials.getUsername(), result.getUsername());
        assertEquals(credentials.getPassword(), result.getPassword());
    }
}
