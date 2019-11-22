package ru.otus.atm;

public class NotAllowedOperation extends RuntimeException {
    public NotAllowedOperation(String s) {
        super(s);
    }
}
