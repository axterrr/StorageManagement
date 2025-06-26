package ua.edu.ukma.clientserver.controller;

import com.sun.net.httpserver.HttpExchange;
import ua.edu.ukma.clientserver.exception.NotFoundException;
import ua.edu.ukma.clientserver.service.StatisticsService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class StatisticsController extends BaseController {

    public static final String STATISTICS_PATH = "/api/statistics";

    private final StatisticsService productService;

    public StatisticsController(StatisticsService productService) {
        this.productService = productService;
    }

    @Override
    protected void handleGet(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (path.matches(STATISTICS_PATH + "/total-price")) {
            handleGetTotalPrice(exchange);
        } else if (path.matches(STATISTICS_PATH + "/total-price/group/\\d+")) {
            handleGetTotalPriceByGroup(exchange);
        } else {
            throw new NotFoundException();
        }
    }

    private void handleGetTotalPriceByGroup(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        Integer groupId = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
        Double totalPrice = productService.getTotalProductsPriceInGroup(groupId);
        String response = "{\"id:\":" + groupId + ",\"totalPrice\":" + totalPrice + "}";
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, responseBytes.length);
        exchange.getResponseBody().write(responseBytes);
    }

    private void handleGetTotalPrice(HttpExchange exchange) throws IOException {
        Double totalPrice = productService.getTotalProductsPriceInStorage();
        String response = "{\"totalPrice\":" + totalPrice + "}";
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, responseBytes.length);
        exchange.getResponseBody().write(responseBytes);
    }
}
