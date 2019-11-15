package ru.otus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SimpleATM implements ATM {
    @Override
    public void insertMoney(Integer... bills) {
        Arrays.stream(bills).parallel()
                .forEach(integer -> Arrays.stream(Bills.values()).parallel().forEach(bills1 -> {
                    if (bills1.getValue() == integer)
                        bills1.addBill();
                }));
    }

    @Override
    public Map<Integer, Integer> getMoney(Integer amount) {
        var outMoneys = new HashMap<Integer, Integer>();
        var outSum = 0;

        for (Bills bill : Bills.values()) {
            var billsCount = 0;
            if (bill.getValue() <= amount && outSum <= amount)
                while (bill.getBills() > 0 && outSum <= amount && (amount - outSum >= bill.getValue())) {
                    bill.removeBill();
                    billsCount++;
                    outMoneys.put(bill.getValue(), billsCount);
                    outSum += bill.getValue();
                }
        }

        return outMoneys;
    }

    @Override
    public Integer getTotal() {
        return Arrays.stream(Bills.values()).parallel()
                .map(bill -> bill.getValue() * bill.getBills())
                .reduce(0, Integer::sum);
    }
}
