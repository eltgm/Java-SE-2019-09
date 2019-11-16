package ru.otus;


import java.util.Objects;

public class BillCellImpl implements BillCell {
    private final int value;
    private int count = 0;

    public BillCellImpl(int value) {
        this.value = value;
    }

    public void addBill() {
        count++;
    }

    public void removeBill() {
        if (count > 0)
            count--;
        else throw new NotEnoughBillException("Нет купюр");
    }

    public int getBills() {
        return count;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "BillCellImpl{" +
                "value=" + value +
                ", count=" + count +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BillCellImpl billCell = (BillCellImpl) o;
        return value == billCell.value &&
                count == billCell.count;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, count);
    }
}
