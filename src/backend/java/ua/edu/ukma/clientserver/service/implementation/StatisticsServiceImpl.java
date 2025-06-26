package ua.edu.ukma.clientserver.service.implementation;

import ua.edu.ukma.clientserver.dao.DaoFactory;
import ua.edu.ukma.clientserver.dao.ProductDao;
import ua.edu.ukma.clientserver.model.Product;
import ua.edu.ukma.clientserver.service.StatisticsService;

import java.util.List;

public class StatisticsServiceImpl implements StatisticsService {

    private final DaoFactory daoFactory;

    private StatisticsServiceImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    private static class Holder {
        static final StatisticsService INSTANCE = new StatisticsServiceImpl(DaoFactory.getDaoFactory());
    }

    public static StatisticsService getInstance() {
        return Holder.INSTANCE;
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
        List<Product> products;
        try (ProductDao productDao = daoFactory.productDao()) {
            products = productDao.getByGroup(groupId);
        }
        return products.stream()
            .mapToDouble(p -> p.getAmount() * p.getPrice())
            .sum();
    }
}
