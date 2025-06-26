package ua.edu.ukma.clientserver.controller;

import com.sun.net.httpserver.HttpExchange;
import ua.edu.ukma.clientserver.exception.NotFoundException;
import ua.edu.ukma.clientserver.model.Credentials;
import ua.edu.ukma.clientserver.service.AuthService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class AuthController extends BaseController {

    public static final String AUTH_PATH = "/api/auth";

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected void handlePost(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (path.matches(AUTH_PATH + "/login")) {
            handlePostLogin(exchange);
        } else {
            throw new NotFoundException();
        }
    }

    private void handlePostLogin(HttpExchange exchange) throws IOException {
        Credentials credentials = objectMapper.readValue(exchange.getRequestBody(), Credentials.class);
        String token = authService.authenticate(credentials.getUsername(), credentials.getPassword());
        String response = "{\"type\":\"Bearer\",\"token\":\"" + token + "\"}";
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, responseBytes.length);
        exchange.getResponseBody().write(responseBytes);
    }
}
