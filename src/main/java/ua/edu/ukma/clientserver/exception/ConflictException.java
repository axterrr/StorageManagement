package ua.edu.ukma.clientserver.exception;

public class ConflictException extends BaseException {

    public ConflictException(String message) {
        super(409, message);
    }

    public ConflictException() {
        this("Conflict occurred");
    }
}
