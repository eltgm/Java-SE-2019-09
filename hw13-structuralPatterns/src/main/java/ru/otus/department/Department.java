package ru.otus.department;

import ru.otus.atm.ATM;

import java.util.ArrayList;
import java.util.List;

public class Department {
    private final List<ATM> atms = new ArrayList<>();

    public void addATM(ATM atm) {
        atms.add(atm);
    }

    public Long getTotal() {
        return atms.stream()
                .map(ATM::getTotal)
                .reduce(0L, Long::sum);
    }

    public void restoreState() {
        atms.forEach(ATM::restoreState);
    }

    public void saveState() {
        atms.forEach(ATM::saveState);
    }
}
