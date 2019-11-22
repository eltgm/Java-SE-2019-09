package ru.otus.atm;

import java.util.List;

public interface ATM {
    List<BillCell> insertMoney(Integer... bills);

    List<BillCell> getMoney(Integer amount);

    Integer getTotal();

    void restoreState();

    void saveState();
}
