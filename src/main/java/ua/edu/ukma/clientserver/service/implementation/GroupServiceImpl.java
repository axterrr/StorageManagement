package ua.edu.ukma.clientserver.service.implementation;

import ua.edu.ukma.clientserver.dao.DaoFactory;
import ua.edu.ukma.clientserver.dao.GroupDao;
import ua.edu.ukma.clientserver.exception.NotFoundException;
import ua.edu.ukma.clientserver.model.Group;
import ua.edu.ukma.clientserver.model.GroupSearchParams;
import ua.edu.ukma.clientserver.service.GroupService;
import ua.edu.ukma.clientserver.validator.GroupValidator;

import java.util.List;

public class GroupServiceImpl implements GroupService {

    private final DaoFactory daoFactory;
    private final GroupValidator validator;

    public GroupServiceImpl(DaoFactory daoFactory, GroupValidator validator) {
        this.daoFactory = daoFactory;
        this.validator = validator;
    }

    @Override
    public List<Group> getAll() {
        try (GroupDao groupDao = daoFactory.groupDao()) {
            return groupDao.getAll();
        }
    }

    @Override
    public Group getById(Integer id) {
        try (GroupDao groupDao = daoFactory.groupDao()) {
            return groupDao.getById(id).orElseThrow(() -> new NotFoundException("Group not found"));
        }
    }

    @Override
    public Integer create(Group group) {
        validator.validateForCreate(group);
        try (GroupDao groupDao = daoFactory.groupDao()) {
            return groupDao.create(group);
        }
    }

    @Override
    public void update(Integer id, Group group) {
        mergeGroup(group, getById(id));
        validator.validateForUpdate(group);
        try (GroupDao groupDao = daoFactory.groupDao()) {
            groupDao.update(group);
        }
    }

    @Override
    public void delete(Integer id) {
        getById(id);
        try (GroupDao groupDao = daoFactory.groupDao()) {
            groupDao.delete(id);
        }
    }

    @Override
    public List<Group> search(GroupSearchParams params) {
        try (GroupDao groupDao = daoFactory.groupDao()) {
            return groupDao.getByParams(params);
        }
    }

    private void mergeGroup(Group group, Group existingGroup) {
        group.setId(existingGroup.getId());
        if (group.getName() == null) {
            group.setName(existingGroup.getName());
        }
        if (group.getDescription() == null) {
            group.setDescription(existingGroup.getDescription());
        }
    }
}
