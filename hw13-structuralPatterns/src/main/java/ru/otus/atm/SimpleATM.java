package ru.otus.atm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleATM implements ATM {
    private final List<BillCell> billCells = new ArrayList<>();
    private SimpleATMOriginator simpleATMOriginator = new SimpleATMOriginator();

    public SimpleATM() {
        Arrays.stream(Bills.values())
                .forEach(bills -> billCells.add(new BillCellImpl(bills.getValue())));
    }

    public SimpleATM(long... bills) {
        Arrays.stream(Bills.values())
                .forEach(billsCell -> billCells.add(new BillCellImpl(billsCell.getValue())));

        Arrays.stream(bills)
                .forEach(integer -> billCells
                        .stream()
                        .filter(billCell -> billCell.getValue() == integer)
                        .forEach(BillCell::addBill));
    }

    public SimpleATM(SimpleATM simpleATM) {
        simpleATM.billCells
                .forEach(billCell -> this.billCells.add(((BillCellImpl) billCell).clone()));
    }

    @Override
    public List<BillCell> insertMoney(long... bills) {
        Arrays.stream(bills)
                .forEach(integer -> billCells
                        .stream()
                        .filter(billCell -> billCell.getValue() == integer)
                        .forEach(BillCell::addBill));

        billCells.stream().forEachOrdered(System.out::println);

        return getReturnedBills(bills);
    }

    private List<BillCell> getReturnedBills(long[] bills) {
        var returnedBills = new ArrayList<BillCell>();

        var returnedBillsLong = getReturnedBillsArray(bills);

        for (var i = 0; i < returnedBillsLong.length; i++) {
            var returnedBill = new BillCellImpl(returnedBillsLong[i]);
            returnedBill.addBill();

            for (var j = 0; j < returnedBillsLong.length; j++) {
                if (returnedBillsLong[i] == returnedBillsLong[j] && i != j) {
                    returnedBill.addBill();
                }
            }
            var toAdd = true;
            for (var bill : returnedBills) {
                if (bill.getValue() == returnedBill.getValue()) {
                    toAdd = false;
                    break;
                }
            }

            if (toAdd)
                returnedBills.add(returnedBill);
        }

        return returnedBills;
    }

    private long[] getReturnedBillsArray(long[] bills) {
        var isAddedBills = new boolean[bills.length];

        for (var i = 0; i < bills.length; i++) {
            for (var billCell : billCells) {
                isAddedBills[i] = billCell.getValue() == bills[i];
                if (isAddedBills[i])
                    break;
            }
        }

        var returnCount = 0;
        for (var isAddedBill : isAddedBills) {
            if (!isAddedBill) {
                returnCount++;
            }
        }
        var returnedBillsLong = new long[returnCount];
        var outCounts = 0;
        for (var i = 0; i < isAddedBills.length; i++) {
            var isAddedBill = isAddedBills[i];
            if (!isAddedBill) {
                returnedBillsLong[outCounts] = bills[i];
                outCounts++;
            }
        }

        return returnedBillsLong;
    }

    @Override
    public List<BillCell> getMoney(long amount) {
        var outMoneys = new ArrayList<BillCell>();
        var outSum = 0;

        for (var bill : billCells) {
            if (bill.getValue() <= amount && outSum <= amount
                    && (amount - outSum >= bill.getValue())) {
                var billCellOut = new BillCellImpl(bill.getValue());
                while (bill.getBills() > 0 && outSum <= amount
                        && (amount - outSum >= bill.getValue())) {
                    bill.removeBill();
                    billCellOut.addBill();
                    outSum += bill.getValue();
                }
                outMoneys.add(billCellOut);
            }
        }

        if (outSum == amount)
            return outMoneys;
        else {
            outMoneys
                    .forEach(billCell -> billCells
                            .forEach(billCell1 -> {
                                if (billCell1.getValue() == billCell.getValue()) {
                                    while (billCell.getBills() > 0) {
                                        billCell1.addBill();
                                        billCell.removeBill();
                                    }
                                }
                            }));
            throw new NotEnoughMoneyException("Недостаточно купюр для выдачи запрашиваемой суммы!");
        }
    }

    @Override
    public long getTotal() {
        return billCells.stream()
                .map(cell -> cell.getValue() * cell.getBills())
                .reduce(0L, Long::sum);
    }

    @Override
    public void restoreState() {
        final var prevATMState = (SimpleATM) simpleATMOriginator.restoreAtm();
        this.billCells.clear();
        prevATMState.billCells.forEach(billCell -> this.billCells.add(((BillCellImpl) billCell).clone()));
    }

    @Override
    public void saveState() {
        simpleATMOriginator.saveAtm(this.clone());
    }

    @Override
    public ATM clone() {
        SimpleATM atm = new SimpleATM();
        this.billCells
                .forEach(billCell -> atm.billCells.add(((BillCellImpl) billCell).clone()));
        return atm;
    }

    @Override
    public String toString() {
        return "SimpleATM{" +
                "billCells=" + billCells +
                '}';
    }
}
