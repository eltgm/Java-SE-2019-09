package ru.otus.department;

import ru.otus.atm.SimpleATM;

import java.util.ArrayDeque;
import java.util.Deque;

class Originator {
    private final Deque<SimpleMemento> stack = new ArrayDeque<>();

    void saveState(SimpleATM atm) {
        stack.push(new SimpleMemento(atm));
    }

    SimpleATM restoreState() {
        return stack.pop().getAtm();
    }
}
