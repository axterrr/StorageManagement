package ua.edu.ukma.clientserver.service;

import java.util.List;

public interface GenericService<T, K> {

	List<T> getAll();

	T getById(K id);

	Integer create(T e);

	void update(T e);

	void delete(K id);
}
