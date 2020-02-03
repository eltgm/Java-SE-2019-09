package ru.otus.hibernate;

import ru.otus.api.dao.UserDao;
import ru.otus.models.AddressDataSet;
import ru.otus.models.User;

public class DbInitializer {
    private final UserDao userDao;

    public DbInitializer(UserDao userDao) {
        this.userDao = userDao;
    }

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

        userDao.saveUser(user);
        var user1 = User.builder()
                .name("Igor")
                .address(AddressDataSet.builder()
                        .street("Rechnaya")
                        .build())
                .login("usertest")
                .password("1234444")
                .role("user")
                .build();
        userDao.saveUser(user1);
    }
}
