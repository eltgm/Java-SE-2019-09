package ru.otus.api.dao;


import ru.otus.api.sessionmanager.SessionManager;
import ru.otus.models.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> findUserById(long id);

    Long saveUser(User user);

    List<User> getAll();

    SessionManager getSessionManager();

    Optional<User> findByLogin(String login);
}
