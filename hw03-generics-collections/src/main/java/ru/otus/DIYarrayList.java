package ru.otus;

import java.util.*;

class DIYarrayList<T> implements List<T> {
    private final int DEFAULT_LENGTH = 10;
    private int size = 0;
    private Object[] store;

    public DIYarrayList() {
        store = new Object[DEFAULT_LENGTH];
    }

    public DIYarrayList(int length) {
        store = new Object[length];
    }

    @Override
    public int size() {
        return store.length;
    }

    @Override
    public boolean isEmpty() {
        return store.length == 0;
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        size = 0;

        for (Object ob : store) {
            if (ob != null)
                size++;
        }
        Object[] outStore = new Object[size];

        int j = 0;
        outer:
        for (int i = 0; i < size; i++) {
            while (j < store.length) {
                if (store[j] == null)
                    continue;
                outStore[i] = store[j];
                j++;
                continue outer;
            }
        }

        return outStore;
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(T t) {
        if (size < store.length) {
            store[size++] = t;
            return true;
        } else {
            int newLength = store.length * 2;
            store = Arrays.copyOf(store, newLength);

            store[size++] = t;
            return true;
        }
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        for (int i = 0; i < store.length; i++) {
            store[i] = null;
        }
    }

    @Override
    public T get(int index) {
        if (index < store.length)
            return (T) store[index];
        return null;
    }

    @Override
    public T set(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T remove(int index) {
        T t = (T) store[index];
        store[index] = null;
        return t;
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<T> listIterator() {
        return new ListIterator<>() {
            private int cursor = -1;

            @Override
            public boolean hasNext() {
                return (cursor < store.length);
            }

            @Override
            public T next() {
                return (T) store[++cursor];
            }

            @Override
            public boolean hasPrevious() {
                return cursor != 0;
            }

            @Override
            public T previous() {
                if (cursor - 1 >= 0) {
                    return (T) store[--cursor];
                }
                return null;
            }

            @Override
            public int nextIndex() {
                return cursor;
            }

            @Override
            public int previousIndex() {
                return cursor - 1;
            }

            @Override
            public void remove() {

            }

            @Override
            public void set(T t) {
                if (cursor < store.length)
                    store[cursor] = t;
            }

            @Override
            public void add(T t) {

            }
        };
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }
}
