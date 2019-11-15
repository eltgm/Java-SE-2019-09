package ru.otus;

public class NotEnoughBillException extends RuntimeException {
    NotEnoughBillException(String s) {
        super(s);
    }
}