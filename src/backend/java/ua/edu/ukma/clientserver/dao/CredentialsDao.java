package ua.edu.ukma.clientserver.dao;

import ua.edu.ukma.clientserver.model.Credentials;

import java.util.Optional;

public interface CredentialsDao extends AutoCloseable {

    Optional<Credentials> getByUsername(String username);

    void close();
}
