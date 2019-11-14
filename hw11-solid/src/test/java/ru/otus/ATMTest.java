package ru.otus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ATMTest {
    private ATM atm;

    @BeforeEach
    void setUp() {
        atm = new SimpleATM();
    }

    @Test
    void insertMoney() {
        var out = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(out));

        atm.insertMoney(10, 10, 10, 5, 15, 50, 100);

        assertEquals(out.toString(), "{50=1, 100=1, 500=0, 1000=0, 10=3}\r\n");
    }

    @Test
    void getMoney() {
        atm.insertMoney(10, 10, 10, 10, 10, 5, 15, 50, 50, 100, 1000);
        final var neededMoney = new HashMap<Integer, Integer>();
        neededMoney.put(10, 0);
        neededMoney.put(50, 1);
        neededMoney.put(100, 0);
        neededMoney.put(500, 0);
        neededMoney.put(1000, 0);

        final var money = atm.getMoney(50);

        assertEquals(neededMoney, money);
    }

    @Test
    void getTotal() {
        atm.insertMoney(10, 10, 10, 5, 15, 50, 100, 10);

        int atmSum = atm.getTotal();

        assertEquals(190, atmSum);
    }
}