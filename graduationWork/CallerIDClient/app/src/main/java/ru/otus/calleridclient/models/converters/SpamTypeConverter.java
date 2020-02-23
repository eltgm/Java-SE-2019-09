package ru.otus.calleridclient.models.converters;

import androidx.room.TypeConverter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SpamTypeConverter {
    @TypeConverter
    public String fromSpamType(List<String> hobbies) {
        return hobbies.stream().collect(Collectors.joining(","));
    }

    @TypeConverter
    public List<String> toSpamType(String data) {
        return Arrays.asList(data.split(","));
    }
}
