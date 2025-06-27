package ua.edu.ukma.clientserver.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.edu.ukma.clientserver.TestUtils;
import ua.edu.ukma.clientserver.dao.DaoFactory;
import ua.edu.ukma.clientserver.dao.GroupDao;
import ua.edu.ukma.clientserver.exception.ValidationException;
import ua.edu.ukma.clientserver.model.Group;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GroupValidatorTest {

    private GroupValidator validator;

    @Mock
    private DaoFactory daoFactory;

    @Mock
    private GroupDao groupDao;

    @BeforeEach
    void beforeEach() {
        validator = new GroupValidator(daoFactory);
        when(daoFactory.groupDao()).thenReturn(groupDao);
    }

    @Test
    void shouldPassValidGroup() {
        validator.validateForCreate(TestUtils.randomGroup());
    }

    @Test
    void shouldFailWithTooLongName() {
        Group group = TestUtils.randomGroup();
        group.setName(TestUtils.randomString(120));
        assertThrows(ValidationException.class, () -> validator.validateForCreate(group));
    }

    @Test
    void shouldFailUpdatingWithExistingName() {
        Group group = TestUtils.randomGroup();
        when(groupDao.getByName(group.getName())).thenReturn(Optional.of(TestUtils.randomGroup()));
        assertThrows(ValidationException.class, () -> validator.validateForUpdate(group));
    }
}
