package ru.otus.atm;

public class NotEnoughMoneyException extends RuntimeException {
    NotEnoughMoneyException(String s) {
        super(s);
    }
}
