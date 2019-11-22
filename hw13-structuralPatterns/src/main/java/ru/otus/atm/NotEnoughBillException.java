package ru.otus.atm;

class NotEnoughBillException extends RuntimeException {
    NotEnoughBillException(String s) {
        super(s);
    }
}