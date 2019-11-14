package ru.otus;

import java.util.Map;

interface ATM {
    void insertMoney(Integer... bills);

    Map<Integer, Integer> getMoney(Integer amount);

    Integer getTotal();
}
