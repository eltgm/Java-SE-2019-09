package ru.otus.api.service.handlers;

import ru.otus.Serializer;
import ru.otus.api.service.DBServiceUser;
import ru.otus.messagesystem.Message;
import ru.otus.messagesystem.MessageType;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.models.User;

import java.util.Optional;

public class CreateUserRequestHandler implements RequestHandler {
    private final DBServiceUser dbService;
    private final Serializer serializer;

    public CreateUserRequestHandler(DBServiceUser dbService, Serializer serializer) {
        this.dbService = dbService;
        this.serializer = serializer;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        final var userToSave = serializer.deserialize(msg.getPayload(), User.class);
        final var userId = dbService.saveUser(userToSave);

        return Optional.of(new Message(msg.getTo(), msg.getFrom(), msg.getId(), MessageType.CREATE_USER.getValue(), serializer.serialize(userId.toString())));
    }
}
