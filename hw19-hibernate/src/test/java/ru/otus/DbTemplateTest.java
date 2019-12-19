package ru.otus;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.otus.api.dao.DBTemplate;
import ru.otus.api.model.AddressDataSet;
import ru.otus.api.model.PhoneDataSet;
import ru.otus.api.model.User;
import ru.otus.api.service.DBService;
import ru.otus.api.service.DbServiceImpl;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.DBTemplateHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DbTemplateTest {
    private static User user;
    private static DBService<User> dbServiceUser;

    @BeforeAll
    public static void beforeAll() {
        user = User.builder()

                .name("Vlad")
                .address(AddressDataSet.builder()

                        .street("Sportivnaya")
                        .build())
                .build();

        user.addPhone(PhoneDataSet.builder()

                .number("+79777777777")
                .user(user)
                .build());

        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory("hibernate.cfg.xml", User.class, AddressDataSet.class, PhoneDataSet.class);

        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        DBTemplate<User> dbTemplate = new DBTemplateHibernate<>(sessionManager);
        dbServiceUser = new DbServiceImpl<>(dbTemplate);

        dbServiceUser.saveObject(user);
    }

    @Test
    public void updateTest() {
        user.setName("Igor");

        dbServiceUser.updateObject(user);

        final var userOptional = dbServiceUser.getObject(1, User.class);
        final var loadedUser = userOptional.orElse(null);
        assertTrue(user.equals(loadedUser));
    }

    @Test
    public void createTest() {
        dbServiceUser.saveObject(user);

        final var userOptional = dbServiceUser.getObject(4, User.class);
        final var loadedUser = userOptional.orElse(null);
        assertTrue(user.equals(loadedUser));
    }

    @Test
    public void loadTest() {
        final var userOptional = dbServiceUser.getObject(4, User.class);
        final var loadedUser = userOptional.orElse(null);

        assertTrue(user.equals(loadedUser));
    }
}
