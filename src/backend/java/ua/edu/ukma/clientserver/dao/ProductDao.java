package ua.edu.ukma.clientserver.dao;

import ua.edu.ukma.clientserver.model.Product;
import ua.edu.ukma.clientserver.model.ProductSearchParams;

import java.util.List;
import java.util.Optional;

public interface ProductDao extends GenericDao<Product, Integer> {

    Optional<Product> getByName(String name);

    List<Product> getByParams(ProductSearchParams params);

    void updateAmount(Integer id, Integer amount);

    List<Product> getByGroup(Integer groupId);
}
