package com.laserdiffraction01.laserdiffraction01.domain;


import lombok.Getter;
import lombok.Setter;
//import org.springframework.lang.NonNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Getter
@Setter
@Entity
public class User {
    //@NonNull //todo: What NotNull annotation is better to choose?
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //todo add validation contraints. And choose better package for them
    private final String name;

    private final String password;

    private final String passwordConfirmation;

    public User() {
        name = "";
        password = "";
        passwordConfirmation = "";
    }

    public User(String name, String password, String passwordConfirmation) {
        this.name = name;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
