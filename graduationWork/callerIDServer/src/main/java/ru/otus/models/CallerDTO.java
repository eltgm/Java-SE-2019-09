package ru.otus.models;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CallerDTO {
    private String telephoneNumber;
    private String spamCategories;
    private String description;
}
