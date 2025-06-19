package ua.edu.ukma.clientserver.dao;

import ua.edu.ukma.clientserver.model.Group;
import ua.edu.ukma.clientserver.model.GroupSearchParams;

import java.util.List;
import java.util.Optional;

public interface GroupDao extends GenericDao<Group, Integer> {

    Optional<Group> getByName(String name);

    List<Group> getByParams(GroupSearchParams params);
}
