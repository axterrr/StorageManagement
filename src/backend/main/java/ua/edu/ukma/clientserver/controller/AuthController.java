package ua.edu.ukma.clientserver.controller;

import com.sun.net.httpserver.HttpExchange;
import ua.edu.ukma.clientserver.exception.NotFoundException;
import ua.edu.ukma.clientserver.model.Credentials;
import ua.edu.ukma.clientserver.service.AuthService;

import java.io.IOException;

import static ua.edu.ukma.clientserver.utils.ControllerUtils.readRequestBody;
import static ua.edu.ukma.clientserver.utils.ControllerUtils.sendJSONResponse;

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
        Credentials credentials = readRequestBody(exchange, Credentials.class);
        String token = authService.authenticate(credentials.getUsername(), credentials.getPassword());
        String response = String.format("{\"type\":\"Bearer\",\"token\":\"%s\"}", token);
        sendJSONResponse(exchange, 200, response);
    }
}
