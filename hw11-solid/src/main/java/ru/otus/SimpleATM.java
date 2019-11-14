package ru.otus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SimpleATM implements ATM {
    private final Map<Integer, Integer> cells = new HashMap<>();

    SimpleATM() {
        cells.put(10, 0);
        cells.put(50, 0);
        cells.put(100, 0);
        cells.put(500, 0);
        cells.put(1000, 0);
    }

    @Override
    public void insertMoney(Integer... bills) {
        Arrays.stream(bills).parallel()
                .filter(this::isValidBill)
                .forEach(this::addBill);
        System.out.println(cells);
    }

    @Override
    public Map<Integer, Integer> getMoney(Integer amount) {
        var outMoneys = new HashMap<Integer, Integer>();
        var outSum = 0;

        var outThousands = 0;
        var outFiveHundreds = 0;
        var outHundreds = 0;
        var outFiftys = 0;
        var outTens = 0;

        if (amount >= 1000) {
            var thousands = cells.get(1000);
            if (thousands > 0)
                while (outSum <= amount && thousands > 0) {
                    outSum += 1000;
                    removeBill(1000);
                    thousands--;
                    outThousands++;
                }
        }

        if (amount >= 500) {
            var fiveHundreds = cells.get(500);
            if (fiveHundreds > 0)
                while (outSum <= amount && fiveHundreds > 0 && (amount - outSum >= 500)) {
                    outSum += 500;
                    removeBill(500);
                    fiveHundreds--;
                    outFiveHundreds++;
                }
        }

        if (amount >= 100) {
            var hundreds = cells.get(100);
            if (hundreds > 0)
                while (outSum <= amount && hundreds > 0 && (amount - outSum >= 100)) {
                    outSum += 100;
                    removeBill(100);
                    hundreds--;
                    outHundreds++;
                }
        }

        if (amount >= 50) {
            var fifty = cells.get(50);
            if (fifty > 0)
                while (outSum <= amount && fifty > 0 && (amount - outSum >= 50)) {
                    outSum += 50;
                    removeBill(50);
                    fifty--;
                    outFiftys++;
                }
        }

        if (amount >= 10) {
            var tens = cells.get(10);
            if (tens > 0)
                while (outSum <= amount && tens > 0 && (amount - outSum >= 10)) {
                    outSum += 10;
                    removeBill(10);
                    tens--;
                    outTens++;
                }
        }

        if (outSum != amount)
            throw new NotEnoughMoneyException("В банкомате недостаточно валют для выдачи суммы!");

        outMoneys.put(10, outTens);
        outMoneys.put(50, outFiftys);
        outMoneys.put(100, outHundreds);
        outMoneys.put(500, outFiveHundreds);
        outMoneys.put(1000, outThousands);

        return outMoneys;
    }

    @Override
    public Integer getTotal() {
        return cells.entrySet().parallelStream()
                .map(integerIntegerEntry -> integerIntegerEntry.getValue() * integerIntegerEntry.getKey())
                .reduce(0, Integer::sum);
    }

    private boolean isValidBill(Integer bill) {
        switch (bill) {
            case 10:
            case 50:
            case 100:
            case 500:
            case 1000:
                return true;
        }
        return false;
    }

    private void addBill(Integer bill) {
        var billCount = cells.get(bill);
        billCount++;
        cells.put(bill, billCount);
    }

    private void removeBill(Integer bill) {
        var billCount = cells.get(bill);
        billCount--;
        cells.put(bill, billCount);
    }
}
