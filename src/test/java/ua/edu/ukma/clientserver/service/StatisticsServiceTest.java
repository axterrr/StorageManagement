package ua.edu.ukma.clientserver.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.edu.ukma.clientserver.dao.DaoFactory;
import ua.edu.ukma.clientserver.dao.GroupDao;
import ua.edu.ukma.clientserver.dao.ProductDao;
import ua.edu.ukma.clientserver.exception.NotFoundException;
import ua.edu.ukma.clientserver.service.implementation.StatisticsServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StatisticsServiceTest {

    private StatisticsService statisticsService;

    @Mock
    private DaoFactory daoFactory;

    @Mock
    private ProductDao productDao;

    @Mock
    private GroupDao groupDao;

    @BeforeEach
    void beforeEach() {
        statisticsService = new StatisticsServiceImpl(daoFactory);
    }

    @Test
    void testShouldReturnZeroWhenNoProducts() {
        when(daoFactory.productDao()).thenReturn(productDao);
        when(productDao.getAll()).thenReturn(List.of());
        assertEquals(0, statisticsService.getTotalProductsPriceInStorage());
    }

    @Test
    void testShouldThrowExceptionWhenUnknownGroup() {
        when(daoFactory.groupDao()).thenReturn(groupDao);
        when(groupDao.getById(1)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> statisticsService.getTotalProductsPriceInGroup(1));
    }
}
