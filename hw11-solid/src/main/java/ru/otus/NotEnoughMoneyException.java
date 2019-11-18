package ru.otus;

class NotEnoughMoneyException extends RuntimeException {
    NotEnoughMoneyException(String s) {
        super(s);
    }
}
