package ru.otus.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;


@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "phones")
public class PhoneDataSet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "number")
    private String number;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneDataSet that = (PhoneDataSet) o;
        return id == that.id &&
                Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, user);
    }
}
