package ru.otus.hibernate;

import org.springframework.stereotype.Component;
import ru.otus.api.dao.UserDao;
import ru.otus.api.model.AddressDataSet;
import ru.otus.api.model.PhoneDataSet;
import ru.otus.api.model.User;

@Component
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

        user.addPhone(PhoneDataSet.builder()
                .number("+79777777777")
                .user(user)
                .build());

        userDao.saveUser(user);
        userDao.saveUser(user);
    }
}
