package ru.otus.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Document(collection = "callers")
public class Caller {
    @Id
    private String telephoneNumber;
    private List<String> spamCategories;
    private String description;
}
