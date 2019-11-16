package ru.otus;

public enum Bills {
    THOUSAND(1000),
    FIVE_HUNDRED(500),
    HUNDRED(100),
    FIFTY(50),
    TEN(10);

    private int value;

    Bills(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
