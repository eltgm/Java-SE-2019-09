package ru.otus;


import java.util.Optional;

public interface RequestHandler {
    Optional<Message> handle(Message msg);
}
