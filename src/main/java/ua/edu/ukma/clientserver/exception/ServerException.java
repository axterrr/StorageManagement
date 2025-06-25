package ua.edu.ukma.clientserver.exception;

public class ServerException extends BaseException {

    public ServerException(String message) {
        super(500, message);
    }

    public ServerException() {
        this("Unexpected server error");
    }
}
