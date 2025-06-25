package ua.edu.ukma.clientserver.exception;

public class NotSupportedMethodException extends BaseException {

    public NotSupportedMethodException(String message) {
        super(405, message);
    }

    public NotSupportedMethodException() {
        this("Method not supported");
    }
}
