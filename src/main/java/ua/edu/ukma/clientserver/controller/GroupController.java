package ua.edu.ukma.clientserver.controller;

import com.sun.net.httpserver.HttpExchange;
import ua.edu.ukma.clientserver.model.Group;
import ua.edu.ukma.clientserver.model.GroupSearchParams;
import ua.edu.ukma.clientserver.service.GroupService;
import ua.edu.ukma.clientserver.utils.QueryUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class GroupController extends BaseController {

    public static final String GROUP_PATH = "/api/group";

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @Override
    protected void handleGet(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (path.matches(GROUP_PATH)) {
            handleGetSearchGroups(exchange);
        } else if (path.matches(GROUP_PATH + "/\\d+")) {
            handleGetGroupById(exchange);
        } else {
            exchange.sendResponseHeaders(404, -1);
        }
    }

    @Override
    protected void handleDelete(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (path.matches(GROUP_PATH + "/\\d+")) {
            handleDeleteGroup(exchange);
        } else {
            exchange.sendResponseHeaders(404, -1);
        }
    }

    @Override
    protected void handlePut(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (path.matches(GROUP_PATH + "/\\d+")) {
            handlePutGroup(exchange);
        } else {
            exchange.sendResponseHeaders(404, -1);
        }
    }

    @Override
    protected void handlePost(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (path.matches(GROUP_PATH)) {
            handlePostGroup(exchange);
        } else {
            exchange.sendResponseHeaders(404, -1);
        }
    }

    private void handlePutGroup(HttpExchange exchange) throws IOException {
        Integer groupId = getGroupId(exchange.getRequestURI().getPath());
        Group group = objectMapper.readValue(exchange.getRequestBody(), Group.class);
        groupService.update(groupId, group);
        exchange.sendResponseHeaders(204, -1);
    }

    private void handlePostGroup(HttpExchange exchange) throws IOException {
        Group group = objectMapper.readValue(exchange.getRequestBody(), Group.class);
        int id = groupService.create(group);
        String response = "{\"id\":" + id + "}";
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(201, responseBytes.length);
        exchange.getResponseBody().write(responseBytes);
    }

    private void handleDeleteGroup(HttpExchange exchange) throws IOException {
        Integer groupId = getGroupId(exchange.getRequestURI().getPath());
        groupService.delete(groupId);
        exchange.sendResponseHeaders(204, -1);
    }

    private void handleGetGroupById(HttpExchange exchange) throws IOException {
        Integer groupId = getGroupId(exchange.getRequestURI().getPath());
        Group group = groupService.getById(groupId);
        byte[] response = objectMapper.writeValueAsBytes(group);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length);
        exchange.getResponseBody().write(response);
    }

    private void handleGetSearchGroups(HttpExchange exchange) throws IOException {
        GroupSearchParams params = QueryUtils.groupSearchParams(exchange.getRequestURI().getQuery());
        List<Group> groups = groupService.search(params);
        byte[] response = objectMapper.writeValueAsBytes(groups);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length);
        exchange.getResponseBody().write(response);
    }

    private Integer getGroupId(String path) {
        return Integer.parseInt(path.substring(GROUP_PATH.length() + 1).split("/")[0]);
    }
}