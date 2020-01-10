package ru.otus.api.model;


import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter

@Entity
@Table(name = "users")
public class User {

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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER)
    @Builder.Default
    private List<PhoneDataSet> phones = new ArrayList<>();

    public void addPhone(PhoneDataSet phone) {
        phones.add(phone);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;

        var phonesEquals = false;
        if (phones.size() == user.phones.size()) {
            for (int i = 0; i < phones.size(); i++) {
                phonesEquals = phones.get(i).equals(user.phones.get(i));
            }
        }
        return id == user.id &&
                Objects.equals(name, user.name) &&
                Objects.equals(address, user.address) &&
                phonesEquals;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, phones);
    }
}
