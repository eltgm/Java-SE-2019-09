package ru.otus.department;

import ru.otus.atm.ATM;

public class SimpleMemento {
    private final ATM atm;

    public SimpleMemento(ATM atm) {
        this.atm = atm;
    }

    public ATM getAtm() {
        return atm;
    }
}