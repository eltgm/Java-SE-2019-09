package ru.otus;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ATMTest {
    private static ATM atm;

    @BeforeAll
    static void setUp() {
        atm = new SimpleATM();
        atm.insertMoney(10, 10, 10, 5, 15, 50, 100);
    }

    @Test
    void insertMoney() {
        assertEquals(3, Bills.TEN.getBills());
        assertEquals(1, Bills.FIFTY.getBills());
        assertEquals(1, Bills.HUNDRED.getBills());
    }

    @Test
    void getTotal() {
        int atmSum = atm.getTotal();

        assertEquals(130, atmSum);
    }

    @Test
    void getMoney() {
        final var neededMoney = new HashMap<Integer, Integer>();
        neededMoney.put(50, 1);

        final var money = atm.getMoney(50);

        assertEquals(neededMoney, money);
    }
}