package com.laserdiffraction01.laserdiffraction01.domain;


import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

//import org.springframework.lang.NonNull;

@Getter
@Setter
@Entity
public class User implements UserDetails {
    //@NonNull //todo: What NotNull annotation is better to choose?
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //todo add validation contraints. And choose better package for them
    private String username;

    private String password;

    private String passwordConfirmation;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();

    public String getFirstRoleNameAsString () {
        if (roles == null || roles.isEmpty()) return "User has no roles";

        return roles.iterator().next().getName();
    }

    public String getRoleNamesAsString () {
        if (roles == null || roles.isEmpty()) return "User has no roles";

        Iterator<Role> it = roles.iterator();

        String resultingRoles = "";

        while ( it.hasNext())
            resultingRoles += it.next().getName() + " ";

        return resultingRoles;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       return getRoles();
    }

    public User() {
        username = "";
        password = "";
        passwordConfirmation = "";
    }

    public User(String username, String password, String passwordConfirmation) {
        this.username = username;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
    }

    public User(String username, String password, String passwordConfirmation, Role userRole) {
        this.username = username;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
        roles.add(userRole);
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

   @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
