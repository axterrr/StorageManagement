package ua.edu.ukma.clientserver.service.implementation;

import ua.edu.ukma.clientserver.dao.DaoFactory;
import ua.edu.ukma.clientserver.dao.GroupDao;
import ua.edu.ukma.clientserver.dao.ProductDao;
import ua.edu.ukma.clientserver.exception.NotFoundException;
import ua.edu.ukma.clientserver.model.Product;
import ua.edu.ukma.clientserver.service.StatisticsService;

import java.util.List;

public class StatisticsServiceImpl implements StatisticsService {

    private final DaoFactory daoFactory;

    public StatisticsServiceImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public Double getTotalProductsPriceInStorage() {
        List<Product> products;
        try (ProductDao productDao = daoFactory.productDao()) {
            products = productDao.getAll();
        }
        return products.stream()
            .mapToDouble(p -> p.getAmount() * p.getPrice())
            .sum();
    }

    @Override
    public Double getTotalProductsPriceInGroup(Integer groupId) {
        try (GroupDao groupDao = daoFactory.groupDao()) {
            if (groupDao.getById(groupId).isEmpty()) {
                throw new NotFoundException("Group not found");
            }
        }

        List<Product> products;
        try (ProductDao productDao = daoFactory.productDao()) {
            products = productDao.getByGroup(groupId);
        }
        return products.stream()
            .mapToDouble(p -> p.getAmount() * p.getPrice())
            .sum();
    }
}
