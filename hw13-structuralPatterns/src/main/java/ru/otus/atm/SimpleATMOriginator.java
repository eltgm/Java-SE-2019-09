package ru.otus.atm;

import ru.otus.department.SimpleMemento;

import java.util.ArrayDeque;
import java.util.Deque;


class SimpleATMOriginator {

    private final Deque<SimpleMemento> stack = new ArrayDeque<>();

    void saveAtm(SimpleATM state) {
        stack.push(new SimpleMemento(state));
    }

    SimpleATM restoreAtm() {
        return stack.pop().getAtm();
    }
}
