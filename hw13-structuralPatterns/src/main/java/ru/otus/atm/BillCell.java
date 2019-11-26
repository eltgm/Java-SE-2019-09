package ru.otus.atm;

public interface BillCell {
    void addBill();

    void removeBill();

    long getBills();

    long getValue();

    BillCell clone();
}
