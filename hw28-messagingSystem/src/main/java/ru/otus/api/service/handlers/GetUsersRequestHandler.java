package ru.otus.api.service.handlers;


import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import ru.otus.Serializers;
import ru.otus.api.service.DBServiceUser;
import ru.otus.messagesystem.Message;
import ru.otus.messagesystem.MessageType;
import ru.otus.messagesystem.RequestHandler;

import java.util.Optional;

@Component
public class GetUsersRequestHandler implements RequestHandler {
    private final DBServiceUser dbService;
    private final Serializers serializer;

    public GetUsersRequestHandler(DBServiceUser dbService, Serializers serializer) {
        this.dbService = dbService;
        this.serializer = serializer;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        final var all = dbService.getAll();

        String data = new Gson().toJson(all);
        return Optional.of(new Message(msg.getTo(), msg.getFrom(), msg.getId(), MessageType.USER_DATA.getValue(), serializer.serialize(data)));
    }
}
