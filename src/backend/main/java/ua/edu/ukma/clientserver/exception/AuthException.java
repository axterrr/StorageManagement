package ua.edu.ukma.clientserver.exception;

public class AuthException extends BaseException {

    public AuthException(String message) {
        super(401, message);
    }

    public AuthException() {
        this("Authentication error");
    }
}
