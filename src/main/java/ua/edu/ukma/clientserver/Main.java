package ua.edu.ukma.clientserver;

import ua.edu.ukma.clientserver.model.Group;
import ua.edu.ukma.clientserver.service.implementation.GroupServiceImpl;

public class Main {
    public static void main(String[] args) {
        GroupServiceImpl.getInstance().create(new Group(0, "test", "test"));
    }
}
