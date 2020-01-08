package ru.otus.hibernate.dao;


import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;
import ru.otus.api.dao.UserDao;
import ru.otus.api.dao.UserDaoException;
import ru.otus.api.model.User;
import ru.otus.api.sessionmanager.SessionManager;
import ru.otus.hibernate.sessionmanager.DatabaseSessionHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class UserDaoHibernate implements UserDao {
    private final SessionManagerHibernate sessionManager;

    public UserDaoHibernate(SessionManagerHibernate sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public Optional<User> findUserById(long id) {
        sessionManager.beginSession();

        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();

        try {
            final var object = currentSession.getHibernateSession().find(User.class, id);
            sessionManager.commitSession();
            return Optional.ofNullable(object);
        } catch (Exception e) {
            sessionManager.rollbackSession();
        } finally {
            sessionManager.close();
        }
        return Optional.empty();
    }

    public Long saveUser(User user) {
        sessionManager.beginSession();
        Long id;
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            id = (Long) hibernateSession.save(user);
            sessionManager.commitSession();
        } catch (Exception e) {
            sessionManager.rollbackSession();
            throw new UserDaoException(e);
        } finally {
            sessionManager.close();
        }

        return id;
    }

    @Override
    public List<User> getAll() {
        sessionManager.beginSession();

        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();

        try {
            final var userList = currentSession.getHibernateSession().createQuery("SELECT a FROM User a", User.class).getResultList();
            sessionManager.commitSession();
            return userList;
        } catch (Exception e) {
            sessionManager.rollbackSession();
        } finally {
            sessionManager.close();
        }
        return Collections.emptyList();
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }

    @Override
    public Optional<User> findByLogin(String login) {
        sessionManager.beginSession();

        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();

        try {
            Query<User> query = currentSession.getHibernateSession().createQuery("from User u where u.login=:login", User.class);
            query.setParameter("login", login);
            final var user = query.uniqueResult();

            sessionManager.commitSession();
            return Optional.ofNullable(user);
        } catch (Exception e) {
            sessionManager.rollbackSession();
        } finally {
            sessionManager.close();
        }
        return Optional.empty();
    }
}
