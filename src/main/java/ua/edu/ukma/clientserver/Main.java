package ua.edu.ukma.clientserver;

import com.sun.net.httpserver.HttpServer;
import ua.edu.ukma.clientserver.controller.GroupController;
import ua.edu.ukma.clientserver.controller.ProductController;
import ua.edu.ukma.clientserver.service.implementation.GroupServiceImpl;
import ua.edu.ukma.clientserver.service.implementation.ProductServiceImpl;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException {
        int port = 8080;

        InetSocketAddress address = new InetSocketAddress(port);
        ProductController productController = new ProductController(ProductServiceImpl.getInstance());
        GroupController groupController = new GroupController(GroupServiceImpl.getInstance());

        HttpServer server = HttpServer.create(address, 0);
        server.createContext(ProductController.PRODUCT_PATH, productController);
        server.createContext(GroupController.GROUP_PATH, groupController);

        server.start();
    }
}