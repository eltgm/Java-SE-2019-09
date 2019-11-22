package ru.otus.department;

import ru.otus.atm.SimpleATM;

public class SimpleMemento {
    private final SimpleATM atm;

    public SimpleMemento(SimpleATM atm) {
        this.atm = new SimpleATM(atm);
    }

    public SimpleATM getAtm() {
        return atm;
    }
}