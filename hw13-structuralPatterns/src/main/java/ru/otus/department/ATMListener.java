package ru.otus.department;

public interface ATMListener {
    long onGetTotal();

    void onRestoreState();

    void onSaveState();
}
