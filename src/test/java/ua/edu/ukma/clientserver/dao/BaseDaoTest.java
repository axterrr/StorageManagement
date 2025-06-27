package ua.edu.ukma.clientserver.dao;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.PostgreSQLContainer;
import ua.edu.ukma.clientserver.dao.jdbc.JdbcCredentialsDao;
import ua.edu.ukma.clientserver.dao.jdbc.JdbcGroupDao;
import ua.edu.ukma.clientserver.dao.jdbc.JdbcProductDao;

import java.sql.Connection;
import java.sql.SQLException;

public class BaseDaoTest {

    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13.4")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test")
            .withInitScript("schema.sql");

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    protected Connection connection;
    protected CredentialsDao credentialsDao;
    protected GroupDao groupDao;
    protected ProductDao productDao;

    @BeforeEach
    void beforeEach() throws SQLException {
        connection = postgres.createConnection("");
        credentialsDao = new JdbcCredentialsDao(connection);
        groupDao = new JdbcGroupDao(connection);
        productDao = new JdbcProductDao(connection);
    }

    @AfterEach
    void afterEach() throws SQLException {
        connection.createStatement().executeUpdate("DELETE FROM credentials");
        groupDao.deleteAll();
        productDao.deleteAll();
        connection.close();
    }
}
