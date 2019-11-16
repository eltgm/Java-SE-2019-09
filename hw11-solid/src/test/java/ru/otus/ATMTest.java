package ru.otus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ATMTest {
    private static ATM atm;

    @BeforeEach
    void setUp() {
        atm = new SimpleATM();
    }

    @Test
    void insertMoney() {
        var out = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(out));
        atm.insertMoney(10, 10, 10, 5, 15, 5, 50, 100);

        assertEquals(out.toString(), "BillCellImpl{value=1000, count=0}\r\n" +
                "BillCellImpl{value=500, count=0}\r\n" +
                "BillCellImpl{value=100, count=1}\r\n" +
                "BillCellImpl{value=50, count=1}\r\n" +
                "BillCellImpl{value=10, count=3}\r\n");
    }

    @Test
    void getTotal() {
        atm.insertMoney(10, 10, 10, 5, 15, 50, 100);

        int atmSum = atm.getTotal();

        assertEquals(180, atmSum);
    }

    @Test
    void getReturnedBills() {
        var returnedBills = new ArrayList<BillCell>();
        BillCell billCellFive = new BillCellImpl(5);
        billCellFive.addBill();
        billCellFive.addBill();
        billCellFive.addBill();
        BillCell billCellFifteen = new BillCellImpl(15);
        billCellFifteen.addBill();
        returnedBills.add(billCellFive);
        returnedBills.add(billCellFifteen);

        final var returnedBillCells = atm.insertMoney(10, 10, 10, 5, 15, 5, 5, 50, 100);
        assertArrayEquals(returnedBills.toArray(), returnedBillCells.toArray());
    }

    @Test
    void getMoney() {
        atm.insertMoney(10, 10, 10, 5, 15, 50, 100);
        final var neededMoney = new ArrayList<BillCell>();
        final var billCell = new BillCellImpl(50);
        billCell.addBill();
        neededMoney.add(billCell);

        final var money = atm.getMoney(50);

        assertArrayEquals(neededMoney.toArray(), money.toArray());
    }
}