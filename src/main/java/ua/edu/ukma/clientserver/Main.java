package ua.edu.ukma.clientserver;

import ua.edu.ukma.clientserver.model.Group;
import ua.edu.ukma.clientserver.model.Product;
import ua.edu.ukma.clientserver.service.implementation.GroupServiceImpl;
import ua.edu.ukma.clientserver.service.implementation.ProductServiceImpl;

public class Main {
    public static void main(String[] args) {
        int id = GroupServiceImpl.getInstance().create(Group.builder().name("test").build());
        ProductServiceImpl.getInstance().create(Product.builder().name("test").price(20.0).groupId(id).build());
    }
}
