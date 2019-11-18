package ru.otus;

import java.util.List;

interface ATM {
    List<BillCell> insertMoney(Integer... bills);

    List<BillCell> getMoney(Integer amount);

    Integer getTotal();
}
