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
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            return Optional.ofNullable(currentSession.getHibernateSession().find(clazz, id));
        } catch (Exception e) {

        }
        return Optional.empty();
    }


    @Override
    public void saveObject(T object) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.save(object);
        } catch (Exception e) {

            throw new UserDaoException(e);
        }
    }

    @Override
    public void updateObject(T object) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.update(object);
        } catch (Exception e) {

            throw new UserDaoException(e);
        }
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
