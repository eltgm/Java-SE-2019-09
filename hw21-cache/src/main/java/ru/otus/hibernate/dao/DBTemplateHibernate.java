package ru.otus.hibernate.dao;


import org.hibernate.Session;
import ru.otus.api.dao.DBTemplate;
import ru.otus.api.dao.UserDaoException;
import ru.otus.api.sessionmanager.SessionManager;
import ru.otus.hibernate.sessionmanager.DatabaseSessionHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.Optional;

public class DBTemplateHibernate<T> implements DBTemplate<T> {
    private final SessionManagerHibernate sessionManager;

    public DBTemplateHibernate(SessionManagerHibernate sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public Optional<T> findById(long id, Class<T> clazz) {
        sessionManager.beginSession();

        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();

        try {
            final var object = currentSession.getHibernateSession().find(clazz, id);
            sessionManager.commitSession();
            return Optional.ofNullable(object);
        } catch (Exception e) {
            sessionManager.rollbackSession();
        } finally {
            sessionManager.close();
        }
        return Optional.empty();
    }


    @Override
    public void saveObject(T object) {
        sessionManager.beginSession();

        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.save(object);
            sessionManager.commitSession();
        } catch (Exception e) {
            sessionManager.rollbackSession();
            throw new UserDaoException(e);
        } finally {
            sessionManager.close();
        }
    }

    @Override
    public void updateObject(T object) {
        sessionManager.beginSession();

        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.merge(object);
            sessionManager.commitSession();
        } catch (Exception e) {
            sessionManager.rollbackSession();

            throw new UserDaoException(e);
        } finally {
            sessionManager.close();
        }
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
