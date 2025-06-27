package ua.edu.ukma.clientserver;

import ua.edu.ukma.clientserver.model.Credentials;
import ua.edu.ukma.clientserver.model.Group;
import ua.edu.ukma.clientserver.model.Product;

import java.util.Random;

public class TestUtils {

    public static final Random random = new Random();

    public static Credentials randomCredentials() {
        return Credentials.builder()
                .id(random.nextInt())
                .username(randomString(10))
                .password(randomString(10))
                .build();
    }

    public static Group randomGroup() {
        return Group.builder()
                .id(random.nextInt())
                .name(randomString(10))
                .description(randomString(10))
                .build();
    }

    public static Product randomProduct(int groupId) {
        return Product.builder()
                .id(random.nextInt())
                .name(randomString(10))
                .description(randomString(10))
                .manufacturer(randomString(10))
                .amount(random.nextInt())
                .price(random.nextDouble())
                .groupId(groupId)
                .build();
    }

    public static String randomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append((char) (Math.random() * 26 + 'a'));
        }
        return sb.toString();
    }
}
