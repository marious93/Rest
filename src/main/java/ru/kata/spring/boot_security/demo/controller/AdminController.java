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
        return "admin/User";
    }

    @PostMapping("/new")
    public String registerUser(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               @RequestParam("firstName") String firstName,
                               @RequestParam("lastName") String lastName,
                               @RequestParam("Age") int age,
                               @RequestParam("roles") List<Integer> roles) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setAge(age);
        userService.saveUser(user, roles);
        return "redirect:/admin/users";
    }

    @GetMapping("/new1")
    public String createUser1(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/User";
    }

    @PostMapping("/new1")
    public String createUser1(@ModelAttribute("newUser") @Validated User user, BindingResult bindingResult,
                              @RequestParam("roles") List<Integer> roles) {
        if (bindingResult.hasErrors()) {
            return "admin/create";
        }
        userService.saveUser(user, roles);
        return "redirect:/admin/users";
    }


//    @GetMapping("/{id}")
//    public String showUser(@PathVariable int id, Model model) {
//        model.addAttribute("user", userService.findUserById(id));
//        return "admin/info";
//    }

    @GetMapping("/users")
    public String showUsersList(Model model) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
//        int a = userService.getUserIdByUsername(userName);
//        if( id==0){
//            id=a;
//        }
        model.addAttribute("user", userService.findUserByUsername(userName));
        model.addAttribute("newUser", new User());
        model.addAttribute("users", userService.getUsersList());
       // model.addAttribute("editUser", userService.findUserById(id));
        model.addAttribute("roles", roleService.findAllRoles());
        return "admin/User";
    }

//    @GetMapping("/edit/{id}")
//    public String updateUser(@PathVariable int id, Model model) {
//        model.addAttribute("user", userService.findUserById(id));
//        return "admin/edit";
//    }
//
//    @PostMapping("/edit/{id}")
//    public String updateUser(@ModelAttribute("user") @Validated User user, BindingResult bindingResult,
//                             @PathVariable int id,
//                             @RequestParam("roles1") List<Integer> roleIds) {
//        if (bindingResult.hasErrors()) {
//            return "admin/edit";
//        }
//        userService.updateUser(id, user, roleIds);
//        return "redirect:/admin/users";
//    }
//
//    @PostMapping("/delete/{id}")
//    public String deleteUser(@PathVariable int id) {
//        userService.deleteUserById(id);
//        return "redirect:/admin/users";
//    }

}
