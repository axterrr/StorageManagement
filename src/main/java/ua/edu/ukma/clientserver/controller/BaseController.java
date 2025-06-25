package ua.edu.ukma.clientserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class BaseController implements HttpHandler {

    protected final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try (exchange) {
            switch (exchange.getRequestMethod()) {
                case "GET" -> handleGet(exchange);
                case "POST" -> handlePost(exchange);
                case "PUT" -> handlePut(exchange);
                case "DELETE" -> handleDelete(exchange);
                default -> exchange.sendResponseHeaders(405, -1);
            }
        }
    }

    protected void handleGet(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(405, -1);
    }

    protected void handlePost(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(405, -1);
    }

    protected void handlePut(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(405, -1);
    }

    protected void handleDelete(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(405, -1);
    }
}