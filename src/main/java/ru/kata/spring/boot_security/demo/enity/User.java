package ru.kata.spring.boot_security.demo.enity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;



import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Data
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "Логин не может быть null")
    @NotEmpty(message = "Логин не может быть пустым")
    @Size(min = 2, max = 30, message = "Логин должен быть от 2 до 30 символов")
    private String firstName;

    @NotNull(message = "Логин не может быть null")
    @NotEmpty(message = "Логин не может быть пустым")
    @Size(min = 2, max = 30, message = "Логин должен быть от 2 до 30 символов")
    private String lastName;

    private int age;

    private String username;


    @NotNull(message = "Пароль не может быть null")
    @NotEmpty(message = "Пароль не может быть пустым")
    @Size(min = 2, max = 20, message = "Пароль должен быть от 2 до 20 символов")
    private String password;

    @ManyToMany
    @JsonManagedReference
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public User() {
    }

    public String getRolesToString() {
        return roles.stream().map(Role::getName).collect(Collectors.joining(" "));
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new HashSet<GrantedAuthority>();
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
