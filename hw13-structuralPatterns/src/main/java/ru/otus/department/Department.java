package ru.otus.department;

import java.util.ArrayList;
import java.util.List;

public class Department {
    private final List<ATMListener> atms = new ArrayList<>();

    public void addATM(ATMListener atm) {
        atms.add(atm);
    }

    public Long getTotal() {
        return atms.stream()
                .map(ATMListener::onGetTotal)
                .reduce(0L, Long::sum);
    }

    public void restoreState() {
        atms.forEach(ATMListener::onRestoreState);
    }

    public void saveState() {
        atms.forEach(ATMListener::onSaveState);
    }
}
