package ua.edu.ukma.clientserver.service.implementation;

import ua.edu.ukma.clientserver.dao.DaoFactory;
import ua.edu.ukma.clientserver.dao.ProductDao;
import ua.edu.ukma.clientserver.model.Product;
import ua.edu.ukma.clientserver.model.ProductSearchParams;
import ua.edu.ukma.clientserver.service.ProductService;
import ua.edu.ukma.clientserver.validator.ProductValidator;

import java.util.List;

public class ProductServiceImpl implements ProductService {

    private final DaoFactory daoFactory;

    private ProductServiceImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    private static class Holder {
        static final ProductService INSTANCE = new ProductServiceImpl(DaoFactory.getDaoFactory());
    }

    public static ProductService getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public Product getById(Integer id) {
        try (ProductDao productDao = daoFactory.productDao()) {
            return productDao.getById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        }
    }

    @Override
    public Integer create(Product product) {
        ProductValidator.getInstance().validateForCreate(product);
        try (ProductDao productDao = daoFactory.productDao()) {
            return productDao.create(product);
        }
    }

    @Override
    public void update(Product product) {
        ProductValidator.getInstance().validateForUpdate(product);
        try (ProductDao productDao = daoFactory.productDao()) {
            productDao.update(product);
        }
    }

    @Override
    public void delete(Integer id) {
        try (ProductDao productDao = daoFactory.productDao()) {
            productDao.delete(id);
        }
    }

    @Override
    public List<Product> search(ProductSearchParams params) {
        try (ProductDao productDao = daoFactory.productDao()) {
            return productDao.getByParams(params);
        }
    }
}
