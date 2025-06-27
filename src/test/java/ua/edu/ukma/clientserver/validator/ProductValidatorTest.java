package ua.edu.ukma.clientserver.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.edu.ukma.clientserver.TestUtils;
import ua.edu.ukma.clientserver.dao.DaoFactory;
import ua.edu.ukma.clientserver.dao.GroupDao;
import ua.edu.ukma.clientserver.dao.ProductDao;
import ua.edu.ukma.clientserver.exception.ValidationException;
import ua.edu.ukma.clientserver.model.Product;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductValidatorTest {

    private ProductValidator validator;

    @Mock
    private DaoFactory daoFactory;

    @Mock
    private ProductDao productDao;

    @Mock
    private GroupDao groupDao;

    @BeforeEach
    void beforeEach() {
        validator = new ProductValidator(daoFactory);
        when(daoFactory.productDao()).thenReturn(productDao);
        when(daoFactory.groupDao()).thenReturn(groupDao);
    }

    @Test
    void shouldPassValidProduct() {
        Product product = TestUtils.randomProduct(1);
        when(groupDao.getById(product.getGroupId())).thenReturn(Optional.of(TestUtils.randomGroup()));
        when(productDao.getByName(product.getName())).thenReturn(Optional.empty());
        validator.validateForCreate(product);
    }

    @Test
    void shouldFailCreatingProductWithExistingName() {
        Product product = TestUtils.randomProduct(1);
        when(groupDao.getById(product.getGroupId())).thenReturn(Optional.of(TestUtils.randomGroup()));
        when(productDao.getByName(product.getName())).thenReturn(Optional.of(TestUtils.randomProduct(1)));
        assertThrows(ValidationException.class, () -> validator.validateForCreate(product));
    }

    @Test
    void shouldFailCreatingProductWithNegativePrice() {
        Product product = TestUtils.randomProduct(1);
        product.setPrice(-1.0);
        when(groupDao.getById(product.getGroupId())).thenReturn(Optional.of(TestUtils.randomGroup()));
        when(productDao.getByName(product.getName())).thenReturn(Optional.of(TestUtils.randomProduct(1)));
        assertThrows(ValidationException.class, () -> validator.validateForCreate(product));
    }
}
