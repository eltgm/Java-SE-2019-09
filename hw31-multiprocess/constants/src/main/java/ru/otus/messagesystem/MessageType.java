package ru.otus.messagesystem;

public enum MessageType {
    USER_DATA("UserData"), CREATE_USER("UserCreate"), CONNECT("Connect");


    private final String value;

    MessageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
