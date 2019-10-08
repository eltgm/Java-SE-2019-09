package ru.otus;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class Main {

    public static void main(String[] args) {
        List<Integer> list = new DIYarrayList<>();
        System.out.println("Start - " + Arrays.toString(list.toArray()));

        Collections.addAll(list, 10, 20, 15, 35, -10,
                38, -1234, 123, 0, -1,
                0, 5, 238432842, -1233413, 12348184,
                1232132135, -1234314, 0, 0, 132, 1);
        System.out.println("After addAll() - " + Arrays.toString(list.toArray()));

        List<Integer> newList = new DIYarrayList<>(list.size());
        Collections.copy(newList, list);
        System.out.println("After copy() - " + Arrays.toString(newList.toArray()));

        Collections.sort(list);
        System.out.println("After sort() - " + Arrays.toString(list.toArray()));
    }
}
