package ua.edu.ukma.clientserver.controller;

import com.sun.net.httpserver.HttpExchange;
import ua.edu.ukma.clientserver.exception.NotFoundException;
import ua.edu.ukma.clientserver.model.Group;
import ua.edu.ukma.clientserver.model.GroupSearchParams;
import ua.edu.ukma.clientserver.service.GroupService;
import ua.edu.ukma.clientserver.utils.ControllerUtils;

import java.io.IOException;
import java.util.List;

import static ua.edu.ukma.clientserver.utils.ControllerUtils.sendJSONResponse;
import static ua.edu.ukma.clientserver.utils.ControllerUtils.sendNoContentResponse;
import static ua.edu.ukma.clientserver.utils.ControllerUtils.readRequestBody;

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
            throw new NotFoundException();
        }
    }

    @Override
    protected void handleDelete(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (path.matches(GROUP_PATH + "/\\d+")) {
            handleDeleteGroup(exchange);
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    protected void handlePut(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (path.matches(GROUP_PATH + "/\\d+")) {
            handlePutGroup(exchange);
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    protected void handlePost(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (path.matches(GROUP_PATH)) {
            handlePostGroup(exchange);
        } else {
            throw new NotFoundException();
        }
    }

    private void handlePutGroup(HttpExchange exchange) throws IOException {
        Integer groupId = getGroupId(exchange.getRequestURI().getPath());
        Group group = readRequestBody(exchange, Group.class);
        groupService.update(groupId, group);
        sendNoContentResponse(exchange);
    }

    private void handlePostGroup(HttpExchange exchange) throws IOException {
        Group group = readRequestBody(exchange, Group.class);
        int id = groupService.create(group);
        String response = String.format("{\"id\":%s}", id);
        sendJSONResponse(exchange, 201, response);
    }

    private void handleDeleteGroup(HttpExchange exchange) throws IOException {
        Integer groupId = getGroupId(exchange.getRequestURI().getPath());
        groupService.delete(groupId);
        sendNoContentResponse(exchange);
    }

    private void handleGetGroupById(HttpExchange exchange) throws IOException {
        Integer groupId = getGroupId(exchange.getRequestURI().getPath());
        Group group = groupService.getById(groupId);
        sendJSONResponse(exchange, 200, group);
    }

    private void handleGetSearchGroups(HttpExchange exchange) throws IOException {
        GroupSearchParams params = ControllerUtils.groupSearchParams(exchange.getRequestURI().getQuery());
        List<Group> groups = groupService.search(params);
        sendJSONResponse(exchange, 200, groups);
    }

    private Integer getGroupId(String path) {
        return Integer.parseInt(path.substring(GROUP_PATH.length() + 1).split("/")[0]);
    }
}
