package ua.edu.ukma.clientserver.dao.jdbc;

import org.postgresql.ds.PGSimpleDataSource;
import ua.edu.ukma.clientserver.config.AppConfig;
import ua.edu.ukma.clientserver.dao.CredentialsDao;
import ua.edu.ukma.clientserver.dao.DaoFactory;
import ua.edu.ukma.clientserver.dao.GroupDao;
import ua.edu.ukma.clientserver.dao.ProductDao;
import ua.edu.ukma.clientserver.exception.ServerException;

import javax.sql.DataSource;
import java.sql.SQLException;

public class JdbcDaoFactory extends DaoFactory {

    private static final String URL_PROPERTY = "database.url";
    private static final String USER_PROPERTY = "database.user";
    private static final String PASSWORD_PROPERTY = "database.password";

    private final DataSource dataSource;

    public JdbcDaoFactory() {
        PGSimpleDataSource dataSourceConfig = new PGSimpleDataSource();
        dataSourceConfig.setUrl(AppConfig.get(URL_PROPERTY));
        dataSourceConfig.setUser(AppConfig.get(USER_PROPERTY));
        dataSourceConfig.setPassword(AppConfig.get(PASSWORD_PROPERTY));
        dataSource = dataSourceConfig;
    }

    @Override
    public ProductDao productDao() {
        try {
            return new JdbcProductDao(dataSource.getConnection());
        } catch (SQLException e) {
            throw new ServerException();
        }
    }

    @Override
    public GroupDao groupDao() {
        try {
            return new JdbcGroupDao(dataSource.getConnection());
        } catch (SQLException e) {
            throw new ServerException();
        }
    }

    @Override
    public CredentialsDao credentialsDao() {
        try {
            return new JdbcCredentialsDao(dataSource.getConnection());
        } catch (SQLException e) {
            throw new ServerException();
        }
    }
}
