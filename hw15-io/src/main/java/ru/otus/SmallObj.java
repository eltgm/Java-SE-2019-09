package ru.otus;

import java.util.Objects;

public class SmallObj {
    private String str;
    private int num;
    private static final String CONST = "Vasya";
    private String nullStr = null;

    public SmallObj(String str, int num) {
        this.str = str;
        this.num = num;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SmallObj smallObj = (SmallObj) o;
        return num == smallObj.num &&
                Objects.equals(str, smallObj.str);
    }

    @Override
    public int hashCode() {
        return Objects.hash(str, num);
    }
}
