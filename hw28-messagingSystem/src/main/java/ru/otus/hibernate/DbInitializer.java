package ru.otus.hibernate;

import org.springframework.stereotype.Service;
import ru.otus.api.dao.UserDao;
import ru.otus.api.model.AddressDataSet;
import ru.otus.api.model.User;

@Service
public class DbInitializer {
    private final UserDao userDao;

    public DbInitializer(UserDao userDao) {
        this.userDao = userDao;
        this.init();
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
