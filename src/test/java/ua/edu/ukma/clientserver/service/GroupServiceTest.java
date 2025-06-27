package ua.edu.ukma.clientserver.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.edu.ukma.clientserver.dao.DaoFactory;
import ua.edu.ukma.clientserver.dao.GroupDao;
import ua.edu.ukma.clientserver.exception.NotFoundException;
import ua.edu.ukma.clientserver.model.Group;
import ua.edu.ukma.clientserver.service.implementation.GroupServiceImpl;
import ua.edu.ukma.clientserver.validator.GroupValidator;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GroupServiceTest {

    private GroupService groupService;

    @Mock
    private DaoFactory daoFactory;

    @Mock
    private GroupDao groupDao;

    @Mock
    private GroupValidator validator;

    @BeforeEach
    void beforeEach() {
        groupService = new GroupServiceImpl(daoFactory, validator);
        when(daoFactory.groupDao()).thenReturn(groupDao);
    }

    @Test
    void testShouldThrowExceptionWhenUnknownId() {
        when(groupDao.getById(1)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> groupService.getById(1));
    }

    @Test
    void testShouldThrowExceptionWhenDeletingUnknownGroup() {
        when(groupDao.getById(1)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> groupService.delete(1));
    }

    @Test
    void testShouldUpdateOnlyOneField() {
        Group groupToUpdate = Group.builder().description("newDescription").build();
        Group existingGroup = Group.builder().name("existingName").build();

        when(groupDao.getById(1)).thenReturn(Optional.of(existingGroup));
        doNothing().when(groupDao).update(groupToUpdate);

        groupService.update(1, groupToUpdate);

        assertEquals("newDescription", groupToUpdate.getDescription());
        assertEquals("existingName", groupToUpdate.getName());
    }
}
