package ua.edu.ukma.clientserver;

import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.HttpServer;
import ua.edu.ukma.clientserver.controller.AuthController;
import ua.edu.ukma.clientserver.controller.GroupController;
import ua.edu.ukma.clientserver.controller.ProductController;
import ua.edu.ukma.clientserver.controller.StatisticsController;
import ua.edu.ukma.clientserver.dao.DaoFactory;
import ua.edu.ukma.clientserver.security.JWTAuthenticator;
import ua.edu.ukma.clientserver.security.JWTUtility;
import ua.edu.ukma.clientserver.service.implementation.AuthServiceImpl;
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
        AuthController authController = new AuthController(AuthServiceImpl.getInstance());
        Authenticator authenticator = new JWTAuthenticator(DaoFactory.getDaoFactory(), JWTUtility.getInstance());

        HttpServer server = HttpServer.create(address, 0);
        server.createContext(ProductController.PRODUCT_PATH, productController).setAuthenticator(authenticator);
        server.createContext(GroupController.GROUP_PATH, groupController).setAuthenticator(authenticator);
        server.createContext(StatisticsController.STATISTICS_PATH, statisticsController).setAuthenticator(authenticator);
        server.createContext(AuthController.AUTH_PATH, authController);

        server.start();
    }
}
