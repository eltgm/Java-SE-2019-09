package ru.otus.calleridclient.models;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity(indices = {@Index(value = {"type"},
        unique = true)})
public class SpamType {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String type;
}
