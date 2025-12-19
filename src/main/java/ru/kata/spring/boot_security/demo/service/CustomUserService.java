package ru.kata.spring.boot_security.demo.service;


import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.enity.Role;
import ru.kata.spring.boot_security.demo.enity.User;

import java.util.List;


public interface CustomUserService extends UserDetailsService {
    public List<User> getUsersList();

    public User findUserByUsername(String username);

    public User findUserById(int id);

    public void saveUser(User user, List<Integer> roleIds);

    public void updateUser(int id, User user, List<Integer> roleIds);

    public void deleteUserById(int id);
    public int getUserIdByUsername(String username);
}
