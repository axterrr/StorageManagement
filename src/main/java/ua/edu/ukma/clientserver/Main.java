package ua.edu.ukma.clientserver;

import com.sun.net.httpserver.HttpServer;
import ua.edu.ukma.clientserver.controller.GroupController;
import ua.edu.ukma.clientserver.controller.ProductController;
import ua.edu.ukma.clientserver.controller.StatisticsController;
import ua.edu.ukma.clientserver.service.implementation.GroupServiceImpl;
import ua.edu.ukma.clientserver.service.implementation.ProductServiceImpl;
import ua.edu.ukma.clientserver.service.implementation.StatisticsServiceImpl;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException {
        InetSocketAddress address = new InetSocketAddress(8080);

        ProductController productController = new ProductController(ProductServiceImpl.getInstance());
        GroupController groupController = new GroupController(GroupServiceImpl.getInstance());
        StatisticsController statisticsController = new StatisticsController(StatisticsServiceImpl.getInstance());

        HttpServer server = HttpServer.create(address, 0);
        server.createContext(ProductController.PRODUCT_PATH, productController);
        server.createContext(GroupController.GROUP_PATH, groupController);
        server.createContext(StatisticsController.STATISTICS_PATH, statisticsController);

        server.start();
    }
}
