package ua.edu.ukma.clientserver;

import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.HttpServer;
import ua.edu.ukma.clientserver.config.AppConfig;
import ua.edu.ukma.clientserver.controller.AuthController;
import ua.edu.ukma.clientserver.controller.GroupController;
import ua.edu.ukma.clientserver.controller.ProductController;
import ua.edu.ukma.clientserver.controller.StatisticsController;
import ua.edu.ukma.clientserver.dao.DaoFactory;
import ua.edu.ukma.clientserver.security.JWTAuthenticator;
import ua.edu.ukma.clientserver.security.JWTUtility;
import ua.edu.ukma.clientserver.security.PasswordHasher;
import ua.edu.ukma.clientserver.service.AuthService;
import ua.edu.ukma.clientserver.service.GroupService;
import ua.edu.ukma.clientserver.service.ProductService;
import ua.edu.ukma.clientserver.service.implementation.AuthServiceImpl;
import ua.edu.ukma.clientserver.service.implementation.GroupServiceImpl;
import ua.edu.ukma.clientserver.service.implementation.ProductServiceImpl;
import ua.edu.ukma.clientserver.service.implementation.StatisticsServiceImpl;
import ua.edu.ukma.clientserver.validator.GroupValidator;
import ua.edu.ukma.clientserver.validator.ProductValidator;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.security.SecureRandom;

public class Main {

    private static final String PORT_PROPERTY = "server.port";
    private static final String KEYSTORE_FILE_PROPERTY = "server.keystore.file";
    private static final String KEYSTORE_PASSWORD_PROPERTY = "server.keystore.password";

    public static void main(String[] args) throws Exception {
      
//        SSLContext sslContext = createSSLContext();
      
        int port = AppConfig.getInt(PORT_PROPERTY);
        InetSocketAddress address = new InetSocketAddress(port);
        HttpServer server = HttpServer.create(address, 0);
  
//        server.setHttpsConfigurator(new HttpsConfigurator(sslContext));

        setupRoutes(server);

        server.start();
        System.out.println("Server started on port: " + port);
    }

    private static void setupRoutes(HttpServer server) {
        DaoFactory daoFactory = DaoFactory.getDaoFactory();
        ProductService productService = new ProductServiceImpl(daoFactory, ProductValidator.getInstance());
        GroupService groupService = new GroupServiceImpl(daoFactory, GroupValidator.getInstance());
        StatisticsServiceImpl statisticsService = new StatisticsServiceImpl(daoFactory);
        AuthService authService = new AuthServiceImpl(daoFactory, JWTUtility.getInstance(), PasswordHasher.getInstance());

        ProductController productController = new ProductController(productService);
        GroupController groupController = new GroupController(groupService);
        StatisticsController statisticsController = new StatisticsController(statisticsService);
        AuthController authController = new AuthController(authService);
        Authenticator authenticator = new JWTAuthenticator(DaoFactory.getDaoFactory(), JWTUtility.getInstance());

        server.createContext(ProductController.PRODUCT_PATH, productController).setAuthenticator(authenticator);
        server.createContext(GroupController.GROUP_PATH, groupController).setAuthenticator(authenticator);
        server.createContext(StatisticsController.STATISTICS_PATH, statisticsController).setAuthenticator(authenticator);
        server.createContext(AuthController.AUTH_PATH, authController);
    }

    private static SSLContext createSSLContext() throws Exception {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        KeyStore ks = KeyStore.getInstance("PKCS12");
        char[] password = AppConfig.get(KEYSTORE_PASSWORD_PROPERTY).toCharArray();

        try (InputStream fis = ClassLoader.getSystemResourceAsStream(AppConfig.get(KEYSTORE_FILE_PROPERTY))) {
            ks.load(fis, password);
        }

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, password);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);

        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());
        return sslContext;
    }
}
