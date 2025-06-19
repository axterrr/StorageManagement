package ua.edu.ukma.clientserver.dao.jdbc;

import org.postgresql.ds.PGSimpleDataSource;
import ua.edu.ukma.clientserver.dao.DaoFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcDaoFactory extends DaoFactory {

    private static final String PROPERTY_FILE = "application.properties";
    private static final String LOCAL_PROPERTY_FILE = "application-local.properties";

    private static JdbcDaoFactory daoFactory;
    private final DataSource dataSource;

    public JdbcDaoFactory() {
        PGSimpleDataSource dataSourceConfig = new PGSimpleDataSource();
        Properties props = new Properties();

        try (InputStream input = JdbcDaoFactory.class.getClassLoader().getResourceAsStream(PROPERTY_FILE)) {
            props.load(input);
            try (InputStream inputLocal = JdbcDaoFactory.class.getClassLoader().getResourceAsStream(LOCAL_PROPERTY_FILE)) {
                if (inputLocal != null) {
                    props.load(inputLocal);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error during reading property file");
        }

        dataSourceConfig.setUrl(props.getProperty("database.url"));
        dataSourceConfig.setUser(props.getProperty("database.user"));
        dataSourceConfig.setPassword(props.getProperty("database.password"));

        dataSource = dataSourceConfig;
    }

    @Override
    public JdbcProductDao goodsDao() {
        try {
            return new JdbcProductDao(dataSource.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JdbcGroupDao groupDao() {
        try {
            return new JdbcGroupDao(dataSource.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static JdbcDaoFactory getDaoFactory() {
        if (daoFactory == null) {
            daoFactory = new JdbcDaoFactory();
        }
        return daoFactory;
    }
}
