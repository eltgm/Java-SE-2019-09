package ru.otus.calleridclient.models;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.calleridclient.models.converters.SpamTypeConverter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity(indices = {@Index(value = {"telephoneNumber"},
        unique = true)})
public class Caller {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @SerializedName("telephoneNumber")
    @Expose
    private String telephoneNumber;
    @TypeConverters({SpamTypeConverter.class})
    private List<String> spamCategories;
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Caller caller = (Caller) o;
        return telephoneNumber.equals(caller.telephoneNumber) &&
                Objects.equals(spamCategories, caller.spamCategories) &&
                Objects.equals(description, caller.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(telephoneNumber, spamCategories, description);
    }
}
