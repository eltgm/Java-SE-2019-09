package ru.otus.department;

import ru.otus.atm.ATM;
import ru.otus.atm.BillCell;
import ru.otus.atm.NotAllowedOperation;

import java.util.ArrayList;
import java.util.List;

public class Department implements ATM {
    private final List<ATM> atms = new ArrayList<>();

    public void addATM(ATM atm) {
        atms.add(atm);
    }


    @Override
    public List<BillCell> insertMoney(Integer... bills) {
        throw new NotAllowedOperation("Insert money not allowed in Department");
    }

    @Override
    public List<BillCell> getMoney(Integer amount) {
        throw new NotAllowedOperation("Get money not allowed in Department");
    }

    @Override
    public Integer getTotal() {
        return atms.stream()
                .map(ATM::getTotal)
                .reduce(0, Integer::sum);
    }

    @Override
    public void restoreState() {
        atms.forEach(ATM::restoreState);
    }

    @Override
    public void saveState() {
        atms.forEach(ATM::saveState);
    }
}
