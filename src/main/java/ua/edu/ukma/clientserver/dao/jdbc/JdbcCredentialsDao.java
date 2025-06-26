package ua.edu.ukma.clientserver.dao.jdbc;

import ua.edu.ukma.clientserver.dao.CredentialsDao;
import ua.edu.ukma.clientserver.exception.ServerException;
import ua.edu.ukma.clientserver.model.Credentials;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class JdbcCredentialsDao implements CredentialsDao {

    private static final String TABLE_NAME = "credentials";

    private static final String ID = "id";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    private final Connection connection;

    public JdbcCredentialsDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Credentials> getByUsername(String username) {
        String sql = String.format("SELECT * FROM %s WHERE %s = ?", TABLE_NAME, USERNAME);

        Credentials credentials = null;
        try (PreparedStatement query = connection.prepareStatement(sql)) {
            query.setString(1, username);
            ResultSet resultSet = query.executeQuery();
            while (resultSet.next()) {
                credentials = extractCredentialsFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new ServerException();
        }

        return Optional.ofNullable(credentials);
    }

    private Credentials extractCredentialsFromResultSet(ResultSet resultSet) throws SQLException {
        return Credentials.builder()
                .id(resultSet.getInt(ID))
                .username(resultSet.getString(USERNAME))
                .password(resultSet.getString(PASSWORD))
                .build();
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new ServerException();
        }
    }
}
