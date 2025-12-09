package ru.kata.spring.boot_security.demo.service;


import ru.kata.spring.boot_security.demo.enity.Role;
import ru.kata.spring.boot_security.demo.enity.User;

import java.util.List;


public interface CustomUserService {
    public List<User> getUsersList();

    public User findUserByUsername(String username);

    public User findUserById(int id);

    public void saveUser(User user);

    public void saveAdmin(User user);

    public void addRole(User user, Role newRole);

    public void updateUser(int id, User user);

    public void deleteUserById(int id);
}
