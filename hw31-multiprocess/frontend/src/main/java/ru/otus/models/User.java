package ru.otus.models;


import lombok.*;

import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter


public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private long id;
    private String name;
    private String login;
    private String password;
    private String role;
}
