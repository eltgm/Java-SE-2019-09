package ru.otus.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;


@AllArgsConstructor
@NoArgsConstructor
@Builder

public class AddressDataSet implements Serializable {
    private static final long serialVersionUID = 2L;
    private long id;

    private String street;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressDataSet that = (AddressDataSet) o;
        return id == that.id &&
                Objects.equals(street, that.street);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, street);
    }
}
