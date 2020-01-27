package ru.otus;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.api.service.DBServiceUser;
import ru.otus.api.service.handlers.CreateUserRequestHandler;
import ru.otus.api.service.handlers.GetUsersRequestHandler;
import ru.otus.front.FrontendService;
import ru.otus.front.handlers.CreateUserResponseHandler;
import ru.otus.front.handlers.GetUsersResponseHandler;
import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.MessageType;
import ru.otus.messagesystem.MsClient;
import ru.otus.messagesystem.MsClientImpl;

@Configuration
public class MessageSystemBeanConfig {
    private static final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";
    private static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";

    @Bean
    public MsClient createDbClient(DBServiceUser dbService, MessageSystem messageSystem, Serializers serializer) {
        MsClient databaseMsClient = new MsClientImpl(serializer, DATABASE_SERVICE_CLIENT_NAME, messageSystem);

        databaseMsClient.addHandler(MessageType.USER_DATA, new GetUsersRequestHandler(dbService, serializer));
        databaseMsClient.addHandler(MessageType.CREATE_USER, new CreateUserRequestHandler(dbService, serializer));
        messageSystem.addClient(databaseMsClient);

        return databaseMsClient;
    }

    @Bean
    public String getDatabaseServiceClientName() {
        return DATABASE_SERVICE_CLIENT_NAME;
    }

    @Bean(name = "frontendMsClient")
    public MsClient createFrontClient(MessageSystem messageSystem, Serializers serializer
            , FrontendService frontendService) {
        MsClient frontendMsClient = new MsClientImpl(serializer, FRONTEND_SERVICE_CLIENT_NAME, messageSystem);

        frontendMsClient.addHandler(MessageType.USER_DATA, new GetUsersResponseHandler(serializer, frontendService));
        frontendMsClient.addHandler(MessageType.CREATE_USER, new CreateUserResponseHandler(serializer, frontendService));
        messageSystem.addClient(frontendMsClient);

        return frontendMsClient;
    }
}
