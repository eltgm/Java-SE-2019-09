package ru.otus.models;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter

@Entity
@Table(name = "users")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    @OneToOne(cascade = CascadeType.ALL, targetEntity = AddressDataSet.class)
    @JoinColumn(name = "addresses_id")
    private AddressDataSet address;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;


        return id == user.id &&
                Objects.equals(name, user.name) &&
                Objects.equals(address, user.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address);
    }
}
