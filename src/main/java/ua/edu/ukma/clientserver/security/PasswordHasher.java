package ua.edu.ukma.clientserver.security;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHasher {

    private PasswordHasher() {}

    private static class Holder {
        static final PasswordHasher INSTANCE = new PasswordHasher();
    }

    public static PasswordHasher getInstance() {
        return PasswordHasher.Holder.INSTANCE;
    }

    public String hash(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public boolean matches(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
