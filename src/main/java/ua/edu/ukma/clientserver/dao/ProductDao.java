package ua.edu.ukma.clientserver.dao;

import ua.edu.ukma.clientserver.model.Product;
import ua.edu.ukma.clientserver.model.ProductSearchParams;

import java.util.List;

public interface ProductDao extends GenericDao<Product, Integer> {

    List<Product> getByParams(ProductSearchParams params);
}
