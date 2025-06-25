package ua.edu.ukma.clientserver.exception;

public class NotFoundException extends BaseException {

    public NotFoundException(String message) {
        super(404, message);
    }

    public NotFoundException() {
        this("Resource not found");
    }
}
