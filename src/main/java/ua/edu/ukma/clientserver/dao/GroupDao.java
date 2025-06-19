package ua.edu.ukma.clientserver.dao;

import ua.edu.ukma.clientserver.model.Group;
import ua.edu.ukma.clientserver.model.GroupSearchParams;

import java.util.List;

public interface GroupDao extends GenericDao<Group, Integer> {

    List<Group> getByParams(GroupSearchParams params);
}
