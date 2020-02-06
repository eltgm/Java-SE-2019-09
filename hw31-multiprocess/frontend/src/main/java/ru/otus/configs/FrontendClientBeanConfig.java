package ru.otus.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.Serializer;
import ru.otus.front.FrontendService;
import ru.otus.front.handlers.CreateUserResponseHandler;
import ru.otus.front.handlers.GetUsersResponseHandler;
import ru.otus.messagesystem.MessageType;
import ru.otus.messagesystem.MsClient;
import ru.otus.messagesystem.MsClientImpl;

@Configuration
public class FrontendClientBeanConfig {
    private static final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";
    private static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";

    @Bean
    public String getDatabaseServiceClientName() {
        return DATABASE_SERVICE_CLIENT_NAME;
    }


    @Bean(name = "frontendMsClient")
    public MsClient createFrontClient(Serializer serializer, FrontendService frontendService) {

        MsClient frontendMsClient = new MsClientImpl(serializer, FRONTEND_SERVICE_CLIENT_NAME);

        frontendMsClient.addHandler(MessageType.USER_DATA, new GetUsersResponseHandler(serializer, frontendService));
        frontendMsClient.addHandler(MessageType.CREATE_USER, new CreateUserResponseHandler(serializer, frontendService));

        return frontendMsClient;
    }
}
