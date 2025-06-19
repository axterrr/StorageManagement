package ua.edu.ukma.clientserver.dao;

import java.util.List;
import java.util.Optional;

public interface GenericDao<T, K> {

	List<T> getAll();

	Optional<T> getById(K id);

	Integer create(T e);

	void update(T e);

	void delete(K id);

	void deleteAll();
}
