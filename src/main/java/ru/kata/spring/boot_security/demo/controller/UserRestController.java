package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.enity.User;
import ru.kata.spring.boot_security.demo.service.CustomUserService;

@RestController
@RequestMapping("/user")
public class UserRestController {
    private final CustomUserService userService;

    public UserRestController(CustomUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/current")
    public User getCurrentUser() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findUserByUsername(userName);
    }

}
