package ua.edu.ukma.clientserver.controller;

import com.sun.net.httpserver.HttpExchange;
import ua.edu.ukma.clientserver.exception.NotFoundException;
import ua.edu.ukma.clientserver.model.Product;
import ua.edu.ukma.clientserver.model.ProductSearchParams;
import ua.edu.ukma.clientserver.service.ProductService;
import ua.edu.ukma.clientserver.utils.QueryUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ProductController extends BaseController {

    public static final String PRODUCT_PATH = "/api/product";

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Override
    protected void handleGet(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (path.matches(PRODUCT_PATH)) {
            handleGetSearchProducts(exchange);
        } else if (path.matches(PRODUCT_PATH + "/\\d+")) {
            handleGetProductById(exchange);
        } if (path.matches(PRODUCT_PATH + "/group/\\d+")) {
            handleGetProductsByGroupId(exchange);
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    protected void handleDelete(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (path.matches(PRODUCT_PATH + "/\\d+")) {
            handleDeleteProduct(exchange);
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    protected void handlePut(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (path.matches(PRODUCT_PATH + "/\\d+")) {
            handlePutProduct(exchange);
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    protected void handlePost(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (path.matches(PRODUCT_PATH)) {
            handlePostProduct(exchange);
        } if (path.matches(PRODUCT_PATH + "/\\d+/increase-amount")) {
            handlePostIncreaseAmount(exchange);
        } if (path.matches(PRODUCT_PATH + "/\\d+/decrease-amount")) {
            handlePostDecreaseAmount(exchange);
        } else {
            throw new NotFoundException();
        }
    }

    private void handlePutProduct(HttpExchange exchange) throws IOException {
        Integer productId = getProductId(exchange.getRequestURI().getPath());
        Product product = objectMapper.readValue(exchange.getRequestBody(), Product.class);
        productService.update(productId, product);
        exchange.sendResponseHeaders(204, -1);
    }

    private void handlePostProduct(HttpExchange exchange) throws IOException {
        Product product = objectMapper.readValue(exchange.getRequestBody(), Product.class);
        int id = productService.create(product);
        String response = "{\"id\":" + id + "}";
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(201, responseBytes.length);
        exchange.getResponseBody().write(responseBytes);
    }

    private void handleDeleteProduct(HttpExchange exchange) throws IOException {
        Integer productId = getProductId(exchange.getRequestURI().getPath());
        productService.delete(productId);
        exchange.sendResponseHeaders(204, -1);
    }

    private void handleGetProductById(HttpExchange exchange) throws IOException {
        Integer productId = getProductId(exchange.getRequestURI().getPath());
        Product product = productService.getById(productId);
        byte[] response = objectMapper.writeValueAsBytes(product);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length);
        exchange.getResponseBody().write(response);
    }

    private void handleGetSearchProducts(HttpExchange exchange) throws IOException {
        ProductSearchParams params = QueryUtils.productSearchParams(exchange.getRequestURI().getQuery());
        List<Product> groups = productService.search(params);
        byte[] response = objectMapper.writeValueAsBytes(groups);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length);
        exchange.getResponseBody().write(response);
    }

    private void handleGetProductsByGroupId(HttpExchange exchange) throws IOException {
        Integer groupId = getGroupId(exchange.getRequestURI().getPath());
        List<Product> groups = productService.getByGroup(groupId);
        byte[] response = objectMapper.writeValueAsBytes(groups);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length);
        exchange.getResponseBody().write(response);
    }

    private void handlePostDecreaseAmount(HttpExchange exchange) throws IOException {
        Product productWithAmount = objectMapper.readValue(exchange.getRequestBody(), Product.class);
        Integer productId = getProductId(exchange.getRequestURI().getPath());
        productService.decreaseAmount(productId, productWithAmount.getAmount());
        exchange.sendResponseHeaders(204, -1);
    }

    private void handlePostIncreaseAmount(HttpExchange exchange) throws IOException {
        Product productWithAmount = objectMapper.readValue(exchange.getRequestBody(), Product.class);
        Integer productId = getProductId(exchange.getRequestURI().getPath());
        productService.increaseAmount(productId, productWithAmount.getAmount());
        exchange.sendResponseHeaders(204, -1);
    }

    private Integer getProductId(String path) {
        return Integer.parseInt(path.substring(PRODUCT_PATH.length() + 1).split("/")[0]);
    }

    private Integer getGroupId(String path) {
        return Integer.parseInt(path.substring(PRODUCT_PATH.length() + "/group/".length()).split("/")[0]);
    }
}
