package ru.otus;

import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Setter
public class TestObj {
    private SmallObj[] objArr;
    private int[] primitiveArr;
    private int primitiveField;
    private List<String> collection;
    private SmallObj obj;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestObj testObj = (TestObj) o;
        return primitiveField == testObj.primitiveField &&
                Arrays.equals(objArr, testObj.objArr) &&
                Arrays.equals(primitiveArr, testObj.primitiveArr) &&
                Objects.equals(collection, testObj.collection) &&
                Objects.equals(obj, testObj.obj);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(primitiveField, collection, obj);
        result = 31 * result + Arrays.hashCode(objArr);
        result = 31 * result + Arrays.hashCode(primitiveArr);
        return result;
    }
}
