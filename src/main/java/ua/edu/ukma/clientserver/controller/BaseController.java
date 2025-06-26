package ua.edu.ukma.clientserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ua.edu.ukma.clientserver.exception.BaseException;
import ua.edu.ukma.clientserver.exception.NotSupportedMethodException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BaseController implements HttpHandler {

    protected final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
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

    private void sendErrorResponse(HttpExchange exchange, int code, String message) throws IOException {
        String response = "{\"code\":" + code + ", \"message\":\"" + message + "\"}";
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(code, responseBytes.length);
        exchange.getResponseBody().write(responseBytes);
    }
}
