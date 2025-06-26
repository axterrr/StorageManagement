package ua.edu.ukma.clientserver.config;

import ua.edu.ukma.clientserver.exception.ServerException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

    private static final String PROPERTY_FILE = "application.properties";
    private static final String LOCAL_PROPERTY_FILE = "application-local.properties";

    private static final Properties props = new Properties();

    static {
        try (InputStream input = ClassLoader.getSystemResourceAsStream(PROPERTY_FILE)) {
            props.load(input);

            try (InputStream local = ClassLoader.getSystemResourceAsStream(LOCAL_PROPERTY_FILE)) {
                if (local != null) {
                    props.load(local);
                }
            }
        } catch (IOException e) {
            throw new ServerException("Failed to load configuration");
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }

    public static long getLong(String key) {
        return Long.parseLong(get(key));
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }
}
