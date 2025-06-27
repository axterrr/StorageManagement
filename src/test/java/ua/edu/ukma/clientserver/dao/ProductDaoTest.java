package ua.edu.ukma.clientserver.dao;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.edu.ukma.clientserver.TestUtils;
import ua.edu.ukma.clientserver.model.Product;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductDaoTest extends BaseDaoTest {

    private int groupId;

    @BeforeEach
    void setUp() {
        groupId = groupDao.create(TestUtils.randomGroup());
    }

    @Test
    void testCreate() {
        Product product = TestUtils.randomProduct(groupId);
        productDao.create(product);
        assertEquals(1, productDao.getAll().size());
    }

    @Test
    void testUpdate() {
        int id = productDao.create(TestUtils.randomProduct(groupId));
        Product newProduct = TestUtils.randomProduct(groupId);
        newProduct.setId(id);
        productDao.update(newProduct);
        Product found = productDao.getById(id).orElseThrow();
        assertEquals(newProduct.getName(), found.getName());
        assertEquals(newProduct.getDescription(), found.getDescription());
    }

    @Test
    void testDelete() {
        int id = productDao.create(TestUtils.randomProduct(groupId));
        productDao.delete(id);
        assertEquals(0, productDao.getAll().size());
    }

    @Test
    void testGetAll() {
        productDao.create(TestUtils.randomProduct(groupId));
        productDao.create(TestUtils.randomProduct(groupId));
        assertEquals(2, productDao.getAll().size());
    }

    @Test
    void testGetById() {
        Product product = TestUtils.randomProduct(groupId);
        int id = productDao.create(product);
        Product result = productDao.getById(id).orElseThrow();
        assertEquals(product.getName(), result.getName());
        assertEquals(product.getDescription(), result.getDescription());
        assertEquals(product.getManufacturer(), result.getManufacturer());
    }

    @Test
    void testGetByName() {
        Product product = TestUtils.randomProduct(groupId);
        int id = productDao.create(product);
        Product result = productDao.getById(id).orElseThrow();
        assertEquals(product.getName(), result.getName());
        assertEquals(product.getDescription(), result.getDescription());
        assertEquals(product.getManufacturer(), result.getManufacturer());
    }
}

