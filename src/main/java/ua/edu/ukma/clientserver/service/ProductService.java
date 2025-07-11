package ua.edu.ukma.clientserver.service;

import ua.edu.ukma.clientserver.model.Product;
import ua.edu.ukma.clientserver.model.ProductSearchParams;

import java.util.List;

public interface ProductService extends GenericService<Product, Integer> {

    List<Product> search(ProductSearchParams params);

    List<Product> getByGroup(Integer groupId);

    void increaseAmount(Integer id, Integer amount);

    void decreaseAmount(Integer id, Integer amount);
}
