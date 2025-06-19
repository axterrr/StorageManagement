package ua.edu.ukma.clientserver.validator;

public interface BaseValidator<T> {

    void validateForCreate(T e);

    void validateForUpdate(T e);
}
