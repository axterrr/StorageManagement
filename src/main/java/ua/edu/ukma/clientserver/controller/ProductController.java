package ua.edu.ukma.clientserver.controller;

import com.sun.net.httpserver.HttpExchange;
import ua.edu.ukma.clientserver.exception.NotFoundException;
import ua.edu.ukma.clientserver.model.Product;
import ua.edu.ukma.clientserver.model.ProductSearchParams;
import ua.edu.ukma.clientserver.service.ProductService;
import ua.edu.ukma.clientserver.utils.ControllerUtils;

import java.io.IOException;
import java.util.List;

import static ua.edu.ukma.clientserver.utils.ControllerUtils.readRequestBody;
import static ua.edu.ukma.clientserver.utils.ControllerUtils.sendJSONResponse;
import static ua.edu.ukma.clientserver.utils.ControllerUtils.sendNoContentResponse;

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
        Product product = readRequestBody(exchange, Product.class);
        productService.update(productId, product);
        sendNoContentResponse(exchange);
    }

    private void handlePostProduct(HttpExchange exchange) throws IOException {
        Product product = readRequestBody(exchange, Product.class);
        int id = productService.create(product);
        String response = String.format("{\"id\":%s}", id);
        sendJSONResponse(exchange, 201, response);
    }

    private void handleDeleteProduct(HttpExchange exchange) throws IOException {
        Integer productId = getProductId(exchange.getRequestURI().getPath());
        productService.delete(productId);
        sendNoContentResponse(exchange);
    }

    private void handleGetProductById(HttpExchange exchange) throws IOException {
        Integer productId = getProductId(exchange.getRequestURI().getPath());
        Product product = productService.getById(productId);
        sendJSONResponse(exchange, 200, product);
    }

    private void handleGetSearchProducts(HttpExchange exchange) throws IOException {
        ProductSearchParams params = ControllerUtils.productSearchParams(exchange.getRequestURI().getQuery());
        List<Product> products = productService.search(params);
        sendJSONResponse(exchange, 200, products);
    }

    private void handleGetProductsByGroupId(HttpExchange exchange) throws IOException {
        Integer groupId = getGroupId(exchange.getRequestURI().getPath());
        List<Product> products = productService.getByGroup(groupId);
        sendJSONResponse(exchange, 200, products);
    }

    private void handlePostDecreaseAmount(HttpExchange exchange) throws IOException {
        Product productWithAmount = readRequestBody(exchange, Product.class);
        Integer productId = getProductId(exchange.getRequestURI().getPath());
        productService.decreaseAmount(productId, productWithAmount.getAmount());
        sendNoContentResponse(exchange);
    }

    private void handlePostIncreaseAmount(HttpExchange exchange) throws IOException {
        Product productWithAmount = readRequestBody(exchange, Product.class);
        Integer productId = getProductId(exchange.getRequestURI().getPath());
        productService.increaseAmount(productId, productWithAmount.getAmount());
        sendNoContentResponse(exchange);
    }

    private Integer getProductId(String path) {
        return Integer.parseInt(path.substring(PRODUCT_PATH.length() + 1).split("/")[0]);
    }

    private Integer getGroupId(String path) {
        return Integer.parseInt(path.substring(PRODUCT_PATH.length() + "/group/".length()).split("/")[0]);
    }
}
