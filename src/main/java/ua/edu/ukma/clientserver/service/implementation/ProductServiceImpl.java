package ua.edu.ukma.clientserver.service.implementation;

import ua.edu.ukma.clientserver.dao.DaoFactory;
import ua.edu.ukma.clientserver.dao.ProductDao;
import ua.edu.ukma.clientserver.exception.ConflictException;
import ua.edu.ukma.clientserver.exception.NotFoundException;
import ua.edu.ukma.clientserver.model.Product;
import ua.edu.ukma.clientserver.model.ProductSearchParams;
import ua.edu.ukma.clientserver.service.ProductService;
import ua.edu.ukma.clientserver.validator.ProductValidator;

import java.util.List;

public class ProductServiceImpl implements ProductService {

    private final DaoFactory daoFactory;
    private final ProductValidator validator;

    public ProductServiceImpl(DaoFactory daoFactory, ProductValidator validator) {
        this.daoFactory = daoFactory;
        this.validator = validator;
    }

    @Override
    public List<Product> getAll() {
        try (ProductDao productDao = daoFactory.productDao()) {
            return productDao.getAll();
        }
    }

    @Override
    public Product getById(Integer id) {
        try (ProductDao productDao = daoFactory.productDao()) {
            return productDao.getById(id).orElseThrow(() -> new NotFoundException("Product not found"));
        }
    }

    @Override
    public Integer create(Product product) {
        validator.validateForCreate(product);
        try (ProductDao productDao = daoFactory.productDao()) {
            return productDao.create(product);
        }
    }

    @Override
    public void update(Integer id, Product product) {
        mergeProduct(product, getById(id));
        validator.validateForUpdate(product);
        try (ProductDao productDao = daoFactory.productDao()) {
            productDao.update(product);
        }
    }

    @Override
    public void delete(Integer id) {
        getById(id);
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

    @Override
    public List<Product> getByGroup(Integer groupId) {
        try (ProductDao productDao = daoFactory.productDao()) {
            return productDao.getByGroup(groupId);
        }
    }

    @Override
    public void increaseAmount(Integer productId, Integer amount) {
        Product product = getById(productId);
        int newAmount = product.getAmount() + amount;
        updateAmount(productId, newAmount);
    }

    @Override
    public void decreaseAmount(Integer productId, Integer amount) {
        Product product = getById(productId);
        int newAmount = product.getAmount() - amount;
        if (newAmount < 0) {
            throw new ConflictException("Not enough products in stock");
        }
        updateAmount(productId, newAmount);
    }

    private void updateAmount(Integer productId, Integer newAmount) {
        try (ProductDao productDao = daoFactory.productDao()) {
            productDao.updateAmount(productId, newAmount);
        }
    }

    private void mergeProduct(Product product, Product existingProduct) {
        product.setId(existingProduct.getId());
        if (product.getName() == null) {
            product.setName(existingProduct.getName());
        }
        if (product.getDescription() == null) {
            product.setDescription(existingProduct.getDescription());
        }
        if (product.getManufacturer() == null) {
            product.setManufacturer(existingProduct.getManufacturer());
        }
        if (product.getPrice() == null) {
            product.setPrice(existingProduct.getPrice());
        }
        if (product.getAmount() == null) {
            product.setAmount(existingProduct.getAmount());
        }
        if (product.getGroupId() == null) {
            product.setGroupId(existingProduct.getGroupId());
        }
    }
}
