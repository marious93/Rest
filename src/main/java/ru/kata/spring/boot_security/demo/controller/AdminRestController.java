package ru.kata.spring.boot_security.demo.controller;


import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.enity.Role;
import ru.kata.spring.boot_security.demo.enity.User;
import ru.kata.spring.boot_security.demo.service.CustomUserService;
import ru.kata.spring.boot_security.demo.service.RoleService;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminRestController {
    private final CustomUserService userService;
    private final RoleService roleService;

    public AdminRestController(CustomUserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable int id) {
        return userService.findUserById(id);
    }

    @GetMapping("/users")
    public List<User> getUserList() {
        return userService.getUsersList();
    }

    @GetMapping("/roles")
    public List<Role> getRoleList() {
        return roleService.findAllRoles();
    }

    @PostMapping("/users")
    public User createUser(@RequestBody @Validated User user) {
        userService.saveUser(user);
        return user;
    }

    @PutMapping("/users")
    public User updateUser(@ModelAttribute("user") @Validated User user) {
        userService.saveUser(user);
        return user;
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) {
        userService.deleteUserById(id);
    }

}
