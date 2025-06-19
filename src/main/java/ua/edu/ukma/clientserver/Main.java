package ua.edu.ukma.clientserver;

import ua.edu.ukma.clientserver.dao.DaoFactory;
import ua.edu.ukma.clientserver.dao.GroupDao;
import ua.edu.ukma.clientserver.model.Group;

public class Main {
    public static void main(String[] args) {
        GroupDao groupDao = DaoFactory.getDaoFactory().groupDao();
        groupDao.create(new Group(0, "test", "test"));
    }
}
