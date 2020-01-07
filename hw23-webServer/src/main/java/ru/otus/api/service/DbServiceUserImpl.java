package ru.otus.api.service;

import ru.otus.api.dao.UserDao;
import ru.otus.api.model.AddressDataSet;
import ru.otus.api.model.PhoneDataSet;
import ru.otus.api.model.User;

import java.util.List;
import java.util.Optional;

public class DbServiceUserImpl implements DBServiceUser {

    private final UserDao userDao;

    public DbServiceUserImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    public Long saveUser(User user) {
        return userDao.saveUser(user);
    }


    public Optional<User> getUser(long id) {
        Optional<User> userOptional = userDao.findUserById(id);

        return userOptional;
    }

    public DbInitializer getInitializer() {
        return new DbInitializer();
    }

    @Override
    public List<User> getAll() {
        return userDao.getAll();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return userDao.findByLogin(login);
    }

    public class DbInitializer {
        public void init() {
            var user = User.builder()
                    .name("Vlad")
                    .address(AddressDataSet.builder()
                            .street("Sportivnaya")
                            .build())
                    .login("testUser")
                    .password("1234444")
                    .role("user")
                    .build();

            user.addPhone(PhoneDataSet.builder()
                    .number("+79777777777")
                    .user(user)
                    .build());

            userDao.saveUser(user);
            userDao.saveUser(user);
        }
    }
}
