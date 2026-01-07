package ru.kata.spring.boot_security.demo.enity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;


import java.util.Objects;
import java.util.Set;

@Entity
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Getter
    private String name;

    @ManyToMany
    @JsonBackReference
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users;

    public Role() {
    }

    public Role(String role) {
        this.name = role;
    }


    @Override
    public String toString() {
        return "Role_" +
                "name='" + name + '\'';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role1 = (Role) o;
        return Objects.equals(name, role1.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }


}
