package ru.otus;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.otus.api.dao.DBTemplate;
import ru.otus.api.model.AddressDataSet;
import ru.otus.api.model.PhoneDataSet;
import ru.otus.api.model.User;
import ru.otus.api.service.DBService;
import ru.otus.api.service.DbServiceImpl;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.HwListener;
import ru.otus.cachehw.MyCache;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.DBTemplateHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DbTemplateTest {
    private static final HwListener<String, User> hwListener =
            (key, value, action) -> System.out.println(action + " value " + value + " with key " + key);
    private static HwCache<String, User> cache;
    private static DBService<User> dbServiceUser;

    @BeforeAll
    public static void beforeAll() {
        User user = User.builder()
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
        cache = new MyCache<>();

        cache.addListener(hwListener);
        dbServiceUser = new DbServiceImpl<>(cache, dbTemplate);

        dbServiceUser.saveObject(user);
    }

    @AfterAll
    public static void afterAll() {
        cache.removeListener(hwListener);
    }

    @Test
    public void getFromCacheTest() {
        long start1 = System.currentTimeMillis();
        final var user = dbServiceUser.getObject(1, User.class);
        long time1 = System.currentTimeMillis() - start1;
        dbServiceUser.updateObject(user.get());
        long start2 = System.currentTimeMillis();
        final var user1 = dbServiceUser.getObject(1, User.class);
        long time2 = System.currentTimeMillis() - start2;

        assertTrue(time2 < time1);
    }

    @Test
    public void cleanCacheWhenGC() throws InterruptedException {
        System.gc();
        Thread.sleep(1000);

        assertNull(cache.get("1"));
    }
}
