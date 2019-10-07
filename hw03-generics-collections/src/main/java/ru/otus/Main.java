package ru.otus;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class Main {

    public static void main(String[] args) {
        List<Integer> list = new DIYarrayList<>();
        System.out.println("Start - " + Arrays.toString(list.toArray()));

        Collections.addAll(list, 10, 20, 15, 35, -10);
        System.out.println("After addAll() - " + Arrays.toString(list.toArray()));

        List<Integer> newList = new DIYarrayList<>();
        Collections.copy(newList, list);
        System.out.println("After copy() - " + Arrays.toString(newList.toArray()));

        Collections.sort(list);
        System.out.println("After sort() - " + Arrays.toString(list.toArray()));
    }
}
