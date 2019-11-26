package ru.otus.atm;

import ru.otus.department.SimpleMemento;

import java.util.ArrayDeque;


class SimpleATMOriginator {

    private final ArrayDeque<SimpleMemento> stack;

    public SimpleATMOriginator() {
        stack = new ArrayDeque<>();
    }

    public SimpleATMOriginator(ArrayDeque<SimpleMemento> stack) {
        this.stack = stack;
    }

    void saveAtm(ATM state) {
        stack.push(new SimpleMemento(state));
    }

    ATM restoreAtm() {
        return stack.pop().getAtm();
    }

    @Override
    protected SimpleATMOriginator clone() {
        return new SimpleATMOriginator(this.stack.clone());
    }
}
