package ru.otus;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QueryHandler {
    String queryString;
    Map<String, Object> queryValues;
}
