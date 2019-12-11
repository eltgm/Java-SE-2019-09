package ru.otus.accountApi.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.otus.Id;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class Account {
    @Id
    long no;
    String type;
    BigDecimal rest;
}
