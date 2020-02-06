package ru.otus;

public interface MessageSystem {

    void addClient(Client client, String clientName);

    void removeClient(String clientId);

    boolean newMessage(Message msg);

    void dispose() throws InterruptedException;
}

