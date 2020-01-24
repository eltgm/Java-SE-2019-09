package ru.otus.api.service.handlers;

import ru.otus.Serializers;
import ru.otus.api.model.User;
import ru.otus.api.service.DBServiceUser;
import ru.otus.messagesystem.Message;
import ru.otus.messagesystem.MessageType;
import ru.otus.messagesystem.RequestHandler;

import java.util.Optional;

public class CreateUserRequestHandler implements RequestHandler {
    private final DBServiceUser dbService;

    public CreateUserRequestHandler(DBServiceUser dbService) {
        this.dbService = dbService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        final var userToSave = Serializers.deserialize(msg.getPayload(), User.class);
        final var userId = dbService.saveUser(userToSave);

        return Optional.of(new Message(msg.getTo(), msg.getFrom(), msg.getId(), MessageType.CREATE_USER.getValue(), Serializers.serialize(userId.toString())));
    }
}
