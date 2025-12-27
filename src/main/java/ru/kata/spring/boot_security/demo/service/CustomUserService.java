package ru.kata.spring.boot_security.demo.service;


import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.enity.User;

import java.util.List;


public interface CustomUserService extends UserDetailsService {
    List<User> getUsersList();

    User findUserByUsername(String username);

    User findUserById(int id);

    void saveUser(User user, List<Integer> roleIds);

    void updateUser(int id, User user, List<Integer> roleIds);

    void deleteUserById(int id);

    int getUserIdByUsername(String username);
}
