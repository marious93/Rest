//package ru.kata.spring.boot_security.demo.controller;
//
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import ru.kata.spring.boot_security.demo.service.CustomUserService;
//
//@Controller
//@RequestMapping("/user")
//public class UserController {
//    private final CustomUserService userService;
//
//    public UserController(CustomUserService userService) {
//        this.userService = userService;
//    }
//
//    @GetMapping()
//    public String showUser(Model model) {
//        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
//        model.addAttribute("user", userService.findUserByUsername(userName));
//        return "private/info";
//    }
//
//}
