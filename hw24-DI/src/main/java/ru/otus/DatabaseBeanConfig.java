package ru.otus;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.api.dao.UserDao;
import ru.otus.api.model.AddressDataSet;
import ru.otus.api.model.PhoneDataSet;
import ru.otus.api.model.User;
import ru.otus.hibernate.DbInitializer;
import ru.otus.hibernate.HibernateUtils;

@Configuration
public class DatabaseBeanConfig {
    @Bean
    public SessionFactory createSessionFactory() {
        return HibernateUtils.buildSessionFactory("hibernate.cfg.xml", User.class, AddressDataSet.class, PhoneDataSet.class);
    }

    @Bean(initMethod = "init")
    public DbInitializer createDbInitializer(UserDao userDao) {
        return new DbInitializer(userDao);
    }
}
