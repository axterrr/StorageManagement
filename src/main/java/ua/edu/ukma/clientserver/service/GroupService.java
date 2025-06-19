package ua.edu.ukma.clientserver.service;

import ua.edu.ukma.clientserver.model.Group;
import ua.edu.ukma.clientserver.model.GroupSearchParams;

import java.util.List;

public interface GroupService extends GenericService<Group, Integer> {

    List<Group> search(GroupSearchParams params);
}
