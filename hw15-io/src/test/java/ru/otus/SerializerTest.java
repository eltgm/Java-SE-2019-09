package ru.otus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SerializerTest {
    private final MyJsonWriter jsonWriter = new MyJsonWriter();
    private TestObj testObj;

    @BeforeEach
    void beforeEach() {
        testObj = new TestObj();

        int[] primitiveArr = new int[5];

        for (int i = 0; i < primitiveArr.length; i++) {
            primitiveArr[i] = i;
        }

        SmallObj[] stringArr = new SmallObj[5];
        stringArr[0] = new SmallObj("first", 0);
        stringArr[1] = new SmallObj("second", 1);
        stringArr[2] = new SmallObj("third", 2);

        List<String> stringList = new ArrayList<>();
        stringList.add("this");
        stringList.add("is");
        stringList.add("collection");

        testObj.setPrimitiveField(15);
        testObj.setCollection(stringList);
        testObj.setPrimitiveArr(primitiveArr);
        testObj.setObjArr(stringArr);
        testObj.setObj(new SmallObj("new", 55));
    }

    @Test
    void writerTest() {
        Gson gson = new Gson();

        final var objJson = jsonWriter.toJson(testObj);
        final var gsonDeserializeObj = gson.fromJson(objJson, TestObj.class);

        assertEquals(testObj, gsonDeserializeObj);
    }

    @Test
    void customWriterTest() {
        Gson gson = new GsonBuilder().serializeNulls().create();

        assertEquals(gson.toJson((byte) 1), jsonWriter.toJson((byte) 1));
        assertEquals(gson.toJson((short) 1f), jsonWriter.toJson((short) 1f));
        assertEquals(gson.toJson(1), jsonWriter.toJson(1));
        assertEquals(gson.toJson(1L), jsonWriter.toJson(1L));
        assertEquals(gson.toJson(1f), jsonWriter.toJson(1f));
        assertEquals(gson.toJson(1d), jsonWriter.toJson(1d));
        assertEquals(gson.toJson("aaa"), jsonWriter.toJson("aaa"));
        assertEquals(gson.toJson('a'), jsonWriter.toJson('a'));
        assertEquals(gson.toJson(new int[]{1, 2, 3}), jsonWriter.toJson(new int[]{1, 2, 3}));
        assertEquals(gson.toJson(List.of(1, 2, 3)), jsonWriter.toJson(List.of(1, 2, 3)));
        assertEquals(gson.toJson(Collections.singletonList(1)), jsonWriter.toJson(Collections.singletonList(1)));
    }
}
