package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.enity.User;
import ru.kata.spring.boot_security.demo.service.CustomUserService;
import ru.kata.spring.boot_security.demo.service.RoleService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final CustomUserService userService;
    private final RoleService roleService;

    public AdminController(CustomUserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/new")
    public String createUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.findAllRoles());
        return "admin/create";
    }

    @PostMapping("/new")
    public String createUser(@ModelAttribute("user") @Validated User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/create";
        }
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/{id}")
    public String showUser(@PathVariable int id, Model model) {
        model.addAttribute("user", userService.findUserById(id));
        return "admin/info";
    }

    @GetMapping()
    public String showUsersList(Model model) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("user", userService.findUserByUsername(userName));
        model.addAttribute("newUser", new User());
        model.addAttribute("users", userService.getUsersList());
        model.addAttribute("userToEdit",new User());
        model.addAttribute("allRoles", roleService.findAllRoles());
        return "admin/users";
    }


    @GetMapping("/edit/{id}")
    public String updateUser(@PathVariable int id, Model model) {
        model.addAttribute("user", userService.findUserById(id));
        model.addAttribute("allRoles",roleService.findAllRoles());
        return "admin/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateUser(@ModelAttribute("user") @Validated User user, BindingResult bindingResult,
                             @PathVariable int id
                             ) {
        if (bindingResult.hasErrors()) {
            return "admin/edit";
        }
        userService.updateUser(id, user);
        return "redirect:/admin";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable int id) {
        userService.deleteUserById(id);
        return "redirect:/admin";
    }

}
