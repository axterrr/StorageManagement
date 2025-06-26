package ua.edu.ukma.clientserver.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import ua.edu.ukma.clientserver.model.GroupSearchParams;
import ua.edu.ukma.clientserver.model.ProductSearchParams;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ControllerUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private ControllerUtils() {}

    public static <T> T readRequestBody(HttpExchange exchange, Class<T> type) throws IOException {
        return objectMapper.readValue(exchange.getRequestBody(), type);
    }

    public static void sendJSONResponse(HttpExchange exchange, int code, String response) throws IOException {
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(code, responseBytes.length);
        exchange.getResponseBody().write(responseBytes);
    }

    public static void sendJSONResponse(HttpExchange exchange, int code, Object response) throws IOException {
        byte[] responseBytes = objectMapper.writeValueAsBytes(response);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(code, responseBytes.length);
        exchange.getResponseBody().write(responseBytes);
    }

    public static void sendNoContentResponse(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(204, -1);
    }

    public static Map<String, String> parseQuery(String query) {
        if (query == null || query.isEmpty()) return Map.of();
        Map<String, String> result = new HashMap<>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                result.put(keyValue[0], keyValue[1]);
            }
        }
        return result;
    }

    public static GroupSearchParams groupSearchParams(String query) {
        Map<String, String> queryParams = parseQuery(query);
        return GroupSearchParams.builder()
                .name(queryParams.get("name"))
                .description(queryParams.get("description"))
                .build();
    }

    public static ProductSearchParams productSearchParams(String query) {
        Map<String, String> queryParams = parseQuery(query);
        return ProductSearchParams.builder()
                .name(queryParams.get("name"))
                .description(queryParams.get("description"))
                .manufacturer(queryParams.get("manufacturer"))
                .amountFrom(parseInt(queryParams.get("amountFrom")))
                .amountTo(parseInt(queryParams.get("amountTo")))
                .priceFrom(parseDouble(queryParams.get("priceFrom")))
                .priceTo(parseDouble(queryParams.get("priceTo")))
                .groupId(parseInt(queryParams.get("groupId")))
                .build();
    }

    private static Double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NullPointerException | NumberFormatException e) {
            return null;
        }
    }

    private static Integer parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
