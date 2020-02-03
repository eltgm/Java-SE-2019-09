package ru.otus;

public class MessageSystemStarter {
    public static void main(String[] args) {
        new MessageSystemStarter().startMessageSystem();
    }

    public void startMessageSystem() {
        MessageSystem messageSystem = new MessageSystemImpl();
    }
}
