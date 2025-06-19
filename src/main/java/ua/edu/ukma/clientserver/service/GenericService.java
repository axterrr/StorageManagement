package ua.edu.ukma.clientserver.service;

public interface GenericService<T, K> {

	T getById(K id);

	Integer create(T e);

	void update(T e);

	void delete(K id);
}
