package ru.otus;

import ru.otus.api.service.DbServiceUserImpl;
import ru.otus.api.service.handlers.CreateUserRequestHandler;
import ru.otus.api.service.handlers.GetUsersRequestHandler;
import ru.otus.hibernate.DbInitializer;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.UserDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.messagesystem.MessageType;
import ru.otus.messagesystem.MsClientImpl;

public class DatabaseClientStarter {
    private static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";

    public static void main(String[] args) {
        new DatabaseClientStarter().connectToMessageServer();
    }

    private void initDbService(Serializer serializer) {

        var sessionFactory = HibernateUtils.buildSessionFactory("hibernate.cfg.xml", ru.otus.models.User.class, ru.otus.models.AddressDataSet.class);

        var sessionManager = new SessionManagerHibernate(sessionFactory);
        var dbTemplate = new UserDaoHibernate(sessionManager);
        var dbService = new DbServiceUserImpl(dbTemplate);

        DbInitializer initializer = new DbInitializer(dbTemplate);
        initializer.init();

        var databaseMsClient = new MsClientImpl(serializer, DATABASE_SERVICE_CLIENT_NAME);

        databaseMsClient.addHandler(MessageType.USER_DATA, new GetUsersRequestHandler(dbService, serializer));
        databaseMsClient.addHandler(MessageType.CREATE_USER, new CreateUserRequestHandler(dbService, serializer));

        databaseMsClient.startListening();
    }

    private void connectToMessageServer() {
        try {
            initDbService(new Serializer());
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }
}
