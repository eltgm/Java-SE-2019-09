package ru.otus.front;


import ru.otus.models.User;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public interface FrontendService {
    void getUsersData(Consumer<String> dataConsumer);

    <T> Optional<Consumer<T>> takeConsumer(UUID sourceMessageId, Class<T> tClass);

    void createUser(Consumer<String> dataConsumer, User user);
}

