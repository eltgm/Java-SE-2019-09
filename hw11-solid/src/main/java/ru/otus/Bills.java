package ru.otus;

public enum Bills {
    THOUSAND(1000),
    FIVE_HUNDRED(500),
    HUNDRED(100),
    FIFTY(50),
    TEN(10);

    private int value;
    private int count;

    Bills(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
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
}
