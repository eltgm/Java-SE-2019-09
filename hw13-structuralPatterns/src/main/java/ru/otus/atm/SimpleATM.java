package ru.otus.atm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleATM implements ATM {
    private final List<BillCell> billCells = new ArrayList<>();
    private final SimpleATMOriginator simpleATMOriginator = new SimpleATMOriginator();

    public SimpleATM() {
        Arrays.stream(Bills.values())
                .forEach(bills -> billCells.add(new BillCellImpl(bills.getValue())));
    }

    public SimpleATM(Integer... bills) {
        Arrays.stream(Bills.values())
                .forEach(billsCell -> billCells.add(new BillCellImpl(billsCell.getValue())));

        Arrays.stream(bills).parallel()
                .forEach(integer -> billCells
                        .stream()
                        .filter(billCell -> billCell.getValue() == integer)
                        .forEach(BillCell::addBill));
    }

    public SimpleATM(SimpleATM simpleATM) {
        simpleATM.billCells.parallelStream().forEach(billCell -> billCells.add(((BillCellImpl) billCell).clone()));
    }

    @Override
    public List<BillCell> insertMoney(Integer... bills) {
        var returnedBills = new ArrayList<BillCell>();

        Arrays.stream(bills)
                .forEach(integer -> billCells
                        .stream()
                        .filter(billCell -> billCell.getValue() == integer)
                        .forEach(BillCell::addBill));

        var isAddedBills = new boolean[bills.length];

        for (var i = 0; i < bills.length; i++) {
            for (var billCell : billCells) {
                isAddedBills[i] = billCell.getValue() == bills[i];
                if (isAddedBills[i])
                    break;
            }
        }

        billCells.parallelStream().forEachOrdered(System.out::println);

        var returnCount = 0;
        for (var isAddedBill : isAddedBills) {
            if (!isAddedBill) {
                returnCount++;
            }
        }
        var returnedBillsInt = new int[returnCount];
        var outCounts = 0;
        for (var i = 0; i < isAddedBills.length; i++) {
            var isAddedBill = isAddedBills[i];
            if (!isAddedBill) {
                returnedBillsInt[outCounts] = bills[i];
                outCounts++;
            }
        }

        for (var i = 0; i < returnedBillsInt.length; i++) {
            var returnedBill = new BillCellImpl(returnedBillsInt[i]);
            returnedBill.addBill();

            for (var j = 0; j < returnedBillsInt.length; j++) {
                if (returnedBillsInt[i] == returnedBillsInt[j] && i != j) {
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

    @Override
    public List<BillCell> getMoney(Integer amount) {
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
            outMoneys.parallelStream()
                    .forEach(billCell -> billCells.parallelStream()
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
    public Integer getTotal() {
        return billCells.parallelStream()
                .map(cell -> cell.getValue() * cell.getBills())
                .reduce(0, Integer::sum);
    }

    @Override
    public void restoreState() {
        final var prevATMState = simpleATMOriginator.restoreAtm();
        this.billCells.clear();
        prevATMState.billCells.parallelStream().forEach(billCell -> this.billCells.add(((BillCellImpl) billCell).clone()));
    }

    @Override
    public void saveState() {
        simpleATMOriginator.saveAtm(this);
    }

    @Override
    public String toString() {
        return "SimpleATM{" +
                "billCells=" + billCells +
                '}';
    }
}
