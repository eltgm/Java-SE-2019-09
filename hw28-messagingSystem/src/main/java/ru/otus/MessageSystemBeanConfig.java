package ru.otus;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.api.dao.UserDao;
import ru.otus.api.service.DBServiceUser;
import ru.otus.api.service.DbServiceUserImpl;
import ru.otus.api.service.handlers.CreateUserRequestHandler;
import ru.otus.api.service.handlers.GetUsersRequestHandler;
import ru.otus.front.FrontendService;
import ru.otus.front.FrontendServiceImpl;
import ru.otus.front.handlers.CreateUserResponseHandler;
import ru.otus.front.handlers.GetUsersResponseHandler;
import ru.otus.messagesystem.*;

@Configuration
public class MessageSystemBeanConfig {
    private static final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";
    private static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";

    @Bean
    public MessageSystem createMessageSystem() {
        return new MessageSystemImpl();
    }

    @Bean
    public DBServiceUser createDbClient(UserDao userDao, MessageSystem messageSystem) {
        MsClient databaseMsClient = new MsClientImpl(DATABASE_SERVICE_CLIENT_NAME, messageSystem);
        DBServiceUser dbService = new DbServiceUserImpl(userDao);
        databaseMsClient.addHandler(MessageType.USER_DATA, new GetUsersRequestHandler(dbService));
        databaseMsClient.addHandler(MessageType.CREATE_USER, new CreateUserRequestHandler(dbService));
        messageSystem.addClient(databaseMsClient);

        return dbService;
    }

    @Bean
    public FrontendService createFrontClient(MessageSystem messageSystem) {
        MsClient frontendMsClient = new MsClientImpl(FRONTEND_SERVICE_CLIENT_NAME, messageSystem);
        FrontendService frontendService = new FrontendServiceImpl(frontendMsClient, DATABASE_SERVICE_CLIENT_NAME);
        frontendMsClient.addHandler(MessageType.USER_DATA, new GetUsersResponseHandler(frontendService));
        frontendMsClient.addHandler(MessageType.CREATE_USER, new CreateUserResponseHandler(frontendService));
        messageSystem.addClient(frontendMsClient);

        return frontendService;
    }
}
