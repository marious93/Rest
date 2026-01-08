package ru.kata.spring.boot_security.demo.controller;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.enity.Role;
import ru.kata.spring.boot_security.demo.enity.User;
import ru.kata.spring.boot_security.demo.service.CustomUserService;
import ru.kata.spring.boot_security.demo.service.RoleService;

import java.util.List;


@RestController
public class MyRestController {
    private final CustomUserService userService;
    private final RoleService roleService;
//    private final String URL = "http://localhost:8080/";

    public MyRestController(CustomUserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }
    @GetMapping("/users/current")
    public User getCurrentUser() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findUserByUsername(userName);
    }

    @GetMapping("/users")
    public List<User> hello() {
        return userService.getUsersList();
    }
    @GetMapping("/roles")
    public List<Role> hello1() {
        return roleService.findAllRoles();
    }

    @PostMapping("/users")
    public User createUser(@ModelAttribute("user") @Validated
                           User user, BindingResult bindingResult) {
        userService.saveUser(user);
        return user;
    }

    @PutMapping("/users")
    public User updateUser(@ModelAttribute("user") @Validated User user, BindingResult bindingResult) {
        userService.saveUser(user);
        return user;
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) {
        userService.deleteUserById(id);
    }

}
