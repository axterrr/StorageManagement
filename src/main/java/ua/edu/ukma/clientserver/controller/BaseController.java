package ua.edu.ukma.clientserver.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ua.edu.ukma.clientserver.exception.BaseException;
import ua.edu.ukma.clientserver.exception.NotSupportedMethodException;

import java.io.IOException;

import static ua.edu.ukma.clientserver.utils.ControllerUtils.sendJSONResponse;

public class BaseController implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) {
        new Thread(() -> {
            try {
                switch (exchange.getRequestMethod()) {
                    case "GET" -> handleGet(exchange);
                    case "POST" -> handlePost(exchange);
                    case "PUT" -> handlePut(exchange);
                    case "DELETE" -> handleDelete(exchange);
                    default -> throw new NotSupportedMethodException();
                }
            } catch (BaseException e) {
                sendErrorResponse(exchange, e.getCode(), e.getMessage());
            } catch (Exception e) {
                sendErrorResponse(exchange, 500, "Unexpected error");
            } finally {
                exchange.close();
            }
        }).start();
    }

    protected void handleGet(HttpExchange exchange) throws IOException {
        throw new NotSupportedMethodException();
    }

    protected void handlePost(HttpExchange exchange) throws IOException {
        throw new NotSupportedMethodException();
    }

    protected void handlePut(HttpExchange exchange) throws IOException {
        throw new NotSupportedMethodException();
    }

    protected void handleDelete(HttpExchange exchange) throws IOException {
        throw new NotSupportedMethodException();
    }

    private void sendErrorResponse(HttpExchange exchange, int code, String message) {
        try {
            String response = String.format("{\"code\":%s, \"message\":\"%s\"}", code, message);
            sendJSONResponse(exchange, code, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
