package ru.otus.atm;

import java.util.List;

public interface ATM extends Cloneable {
    List<BillCell> insertMoney(long... bills);

    List<BillCell> getMoney(long amount);

    long getTotal();

    void restoreState();

    void saveState();

    ATM clone();
}
