package ru.otus.api.service;

import ru.otus.api.model.User;

import java.util.List;
import java.util.Optional;

public interface DBServiceUser {

    Long saveUser(User object);

    Optional<User> getUser(long id);

    List<User> getAll();

    Optional<User> findByLogin(String login);
}
