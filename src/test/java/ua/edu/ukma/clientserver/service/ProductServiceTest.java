package ua.edu.ukma.clientserver.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.edu.ukma.clientserver.dao.DaoFactory;
import ua.edu.ukma.clientserver.dao.ProductDao;
import ua.edu.ukma.clientserver.exception.ConflictException;
import ua.edu.ukma.clientserver.exception.NotFoundException;
import ua.edu.ukma.clientserver.model.Product;
import ua.edu.ukma.clientserver.service.implementation.ProductServiceImpl;
import ua.edu.ukma.clientserver.validator.ProductValidator;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private DaoFactory daoFactory;

    @Mock
    private ProductDao productDao;

    @Mock
    private ProductValidator validator;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl(daoFactory, validator);
        when(daoFactory.productDao()).thenReturn(productDao);
    }

    @Test
    void testThrowsNotFoundExceptionWhenUnknownId() {
        when(productDao.getById(1)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> productService.getById(1));
    }

    @Test
    void testThrowsConflictExceptionWhenDecreaseAmountToNegative() {
        when(productDao.getById(1)).thenReturn(Optional.of(Product.builder().amount(0).build()));
        assertThrows(ConflictException.class, () -> productService.decreaseAmount(1, 10));
    }

    @Test
    void testUpdateOnlySomeFields() {
        Product productToUpdate = Product.builder().description("newDescription").build();
        Product existingProduct = Product.builder().name("existingName").build();

        when(productDao.getById(1)).thenReturn(Optional.of(existingProduct));
        doNothing().when(productDao).update(productToUpdate);

        productService.update(1, productToUpdate);
        verify(productDao).update(productToUpdate);

        assertEquals("newDescription", productToUpdate.getDescription());
        assertEquals("existingName", productToUpdate.getName());
    }
}
