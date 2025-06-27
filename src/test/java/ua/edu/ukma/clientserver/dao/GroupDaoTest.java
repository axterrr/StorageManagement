package ua.edu.ukma.clientserver.dao;

import org.junit.jupiter.api.Test;
import ua.edu.ukma.clientserver.TestUtils;
import ua.edu.ukma.clientserver.model.Group;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GroupDaoTest extends BaseDaoTest {

    @Test
    void testCreate() {
        Group group = TestUtils.randomGroup();
        groupDao.create(group);
        assertEquals(1, groupDao.getAll().size());
    }

    @Test
    void testUpdate() {
        int id = groupDao.create(TestUtils.randomGroup());
        Group newGroup = TestUtils.randomGroup();
        newGroup.setId(id);
        groupDao.update(newGroup);
        Group found = groupDao.getById(id).orElseThrow();
        assertEquals(newGroup.getName(), found.getName());
        assertEquals(newGroup.getDescription(), found.getDescription());
    }

    @Test
    void testDelete() {
        int id = groupDao.create(TestUtils.randomGroup());
        groupDao.delete(id);
        assertEquals(0, groupDao.getAll().size());
    }

    @Test
    void testGetAll() {
        groupDao.create(TestUtils.randomGroup());
        groupDao.create(TestUtils.randomGroup());
        assertEquals(2, groupDao.getAll().size());
    }

    @Test
    void testGetById() {
        Group group = TestUtils.randomGroup();
        int id = groupDao.create(group);
        group.setId(id);
        assertEquals(group, groupDao.getById(id).orElseThrow());
    }

    @Test
    void testGetByName() {
        Group group = TestUtils.randomGroup();
        int id = groupDao.create(group);
        group.setId(id);
        assertEquals(group, groupDao.getByName(group.getName()).orElseThrow());
    }
}
