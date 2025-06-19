package ua.edu.ukma.clientserver.dao;

import ua.edu.ukma.clientserver.dao.jdbc.JdbcDaoFactory;

public abstract class DaoFactory {

    private static JdbcDaoFactory daoFactory;

    public static DaoFactory getDaoFactory() {
        if (daoFactory == null) {
            daoFactory = new JdbcDaoFactory();
        }
        return daoFactory;
    }

    public abstract ProductDao productDao();

    public abstract GroupDao groupDao();
}
