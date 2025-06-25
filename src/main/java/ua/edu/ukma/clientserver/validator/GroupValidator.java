package ua.edu.ukma.clientserver.validator;

import ua.edu.ukma.clientserver.dao.DaoFactory;
import ua.edu.ukma.clientserver.dao.GroupDao;
import ua.edu.ukma.clientserver.exception.ValidationException;
import ua.edu.ukma.clientserver.model.Group;

import java.util.ArrayList;
import java.util.Optional;

public class GroupValidator implements BaseValidator<Group> {

    private final DaoFactory daoFactory;

    private GroupValidator(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    private static class Holder {
        static final GroupValidator INSTANCE = new GroupValidator(DaoFactory.getDaoFactory());
    }

    public static GroupValidator getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public void validateForCreate(Group group) {
        ArrayList<String> errors = commonValidate(group);
        try (GroupDao groupDao = daoFactory.groupDao()) {
            if (groupDao.getByName(group.getName()).isPresent()) {
                errors.add("Group with this name already exists");
            }
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors.toString());
        }
    }

    @Override
    public void validateForUpdate(Group group) {
        ArrayList<String> errors = commonValidate(group);
        try (GroupDao groupDao = daoFactory.groupDao()) {
            Optional<Group> existingGroup = groupDao.getByName(group.getName());
            if (existingGroup.isPresent() && !existingGroup.get().getId().equals(group.getId())) {
                errors.add("Group with this name already exists");
            }
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors.toString());
        }
    }

    private ArrayList<String> commonValidate(Group group) {
        ArrayList<String> errors = new ArrayList<>();
        validateName(errors, group.getName());
        return errors;
    }

    private void validateName(ArrayList<String> errors, String name) {
        if (name == null || name.isEmpty()) {
            errors.add("Name is required");
        }
        if (name != null && name.length() > 100) {
            errors.add("Name is too long");
        }
    }
}