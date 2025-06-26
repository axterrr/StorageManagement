package ua.edu.ukma.clientserver.exception;

public class ValidationException extends BaseException {

    public ValidationException(String message) {
        super(400, message);
    }

    public ValidationException() {
        this("Validation error");
    }
}
