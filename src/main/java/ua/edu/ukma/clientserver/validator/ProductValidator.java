package ua.edu.ukma.clientserver.validator;

import ua.edu.ukma.clientserver.dao.DaoFactory;
import ua.edu.ukma.clientserver.dao.GroupDao;
import ua.edu.ukma.clientserver.dao.ProductDao;
import ua.edu.ukma.clientserver.exception.ValidationException;
import ua.edu.ukma.clientserver.model.Product;

import java.util.ArrayList;
import java.util.Optional;

public class ProductValidator implements BaseValidator<Product> {

    private final DaoFactory daoFactory;

    private ProductValidator(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    private static class Holder {
        static final ProductValidator INSTANCE = new ProductValidator(DaoFactory.getDaoFactory());
    }

    public static ProductValidator getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public void validateForCreate(Product product) {
        ArrayList<String> errors = commonValidate(product);
        try (ProductDao productDao = daoFactory.productDao()) {
            if (productDao.getByName(product.getName()).isPresent()) {
                errors.add("Product with this name already exists");
            }
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors.toString());
        }
    }

    @Override
    public void validateForUpdate(Product product) {
        ArrayList<String> errors = commonValidate(product);
        try (ProductDao productDao = daoFactory.productDao()) {
            Optional<Product> existingProduct = productDao.getByName(product.getName());
            if (existingProduct.isPresent() && !existingProduct.get().getId().equals(product.getId())) {
                errors.add("Product with this name already exists");
            }
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors.toString());
        }
    }

    private ArrayList<String> commonValidate(Product product) {
        ArrayList<String> errors = new ArrayList<>();
        validateName(errors, product.getName());
        validateManufacturer(errors, product.getManufacturer());
        validateAmount(errors, product.getAmount());
        validatePrice(errors, product.getPrice());
        validateGroupId(errors, product.getGroupId());
        return errors;
    }

    private void validateGroupId(ArrayList<String> errors, Integer groupId) {
        if (groupId == null) {
            errors.add("Group ID is required");
        } else {
            try (GroupDao groupDao = daoFactory.groupDao()) {
                if (groupDao.getById(groupId).isEmpty()) {
                    errors.add("Group with this ID does not exist");
                }
            }
        }
    }

    private void validatePrice(ArrayList<String> errors, Double price) {
        if (price == null) {
            errors.add("Price is required");
        }
        if (price != null && price < 0) {
            errors.add("Price must be greater than zero");
        }
    }

    private void validateAmount(ArrayList<String> errors, Integer amount) {
        if (amount != null && amount < 0) {
            errors.add("Amount must be greater than zero");
        }
    }

    private void validateManufacturer(ArrayList<String> errors, String manufacturer) {
        if (manufacturer != null && manufacturer.length() > 100) {
            errors.add("Manufacturer is too long");
        }
    }

    private void validateName(ArrayList<String> errors, String name) {
        if (name == null || name.isEmpty()) {
            errors.add("Name is required");
        }
        if (name != null && name.length() > 100) {
            errors.add("Name is too long");
        }
    }
}