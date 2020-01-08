package ru.otus.api.service;

import org.springframework.stereotype.Component;
import ru.otus.api.dao.UserDao;
import ru.otus.api.model.User;
import ru.otus.hibernate.DbInitializer;

import java.util.List;
import java.util.Optional;

@Component
public class DbServiceUserImpl implements DBServiceUser {
    private final DbInitializer dbInitializer;
    private final UserDao userDao;

    public DbServiceUserImpl(UserDao userDao) {
        this.userDao = userDao;
        this.dbInitializer = new DbInitializer(userDao);
        dbInitializer.init();
    }

    public Long saveUser(User user) {
        return userDao.saveUser(user);
    }


    public Optional<User> getUser(long id) {
        Optional<User> userOptional = userDao.findUserById(id);

        return userOptional;
    }

    public DbInitializer getInitializer() {
        return dbInitializer;
    }

    @Override
    public List<User> getAll() {
        return userDao.getAll();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return userDao.findByLogin(login);
    }
}
