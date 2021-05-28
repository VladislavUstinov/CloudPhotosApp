package com.laserdiffraction01.laserdiffraction01.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Role implements GrantedAuthority {

    public static String USER_ROLE_STRING = "USER";
    public static String ADMIN_ROLE_STRING = "ADMIN";

    @Id
    private Long id;

    private String name;

    public String getName () { return "ROLE_" + name;} //какая жесть.. 1.5 часа потратил, чтобы добавить этот префикс!!
    public void setName (String name) { this.name = name;}

    //@Transient - думаю, не надо. Уж или связь reference (не делать связь с этой стороны вообще),
    //             или не стоит ее объявлять, а потом ставить transient, чтобы она не хранилась

    @ManyToMany(mappedBy = "roles")
    private List<User> users = new ArrayList<>();

    public Role() {
    }

    public Role(Long id) {
        this.id = id;
    }

    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }


    @Override
    public String getAuthority() {
        return getName();
    }
}
