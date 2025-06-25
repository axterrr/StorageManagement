package ua.edu.ukma.clientserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ua.edu.ukma.clientserver.exception.BaseException;
import ua.edu.ukma.clientserver.exception.NotSupportedMethodException;
import ua.edu.ukma.clientserver.model.ErrorResponse;

import java.io.IOException;

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
        ErrorResponse errorResponse = new ErrorResponse(code, message);
        byte[] response = objectMapper.writeValueAsBytes(errorResponse);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(errorResponse.getCode(), response.length);
        exchange.getResponseBody().write(response);
    }
}
