package ru.otus.atm;


import java.util.Objects;

public class BillCellImpl implements BillCell {
    private final long value;
    private long count = 0;

    public BillCellImpl(long value) {
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

    public long getBills() {
        return count;
    }

    @Override
    public long getValue() {
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

    @Override
    public BillCellImpl clone() {
        BillCellImpl billCell = new BillCellImpl(this.getValue());
        billCell.count = this.getBills();
        return billCell;
    }
}
