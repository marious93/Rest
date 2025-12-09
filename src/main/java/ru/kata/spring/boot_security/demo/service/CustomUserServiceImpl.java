package ru.kata.spring.boot_security.demo.service;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.enity.Role;
import ru.kata.spring.boot_security.demo.enity.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomUserServiceImpl implements UserDetailsService, CustomUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private static final String USER_ROLE = "ROLE_USER";
    private static final String ADMIN_ROLE = "ROLE_ADMIN";

    public CustomUserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<User> getUsersList() {
        return userRepository.findAll();
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findUserById(int id) {
        return userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public void saveUser(User user) {
        Role role = roleRepository.findByName(USER_ROLE);
        if (role == null) {
            role = new Role(USER_ROLE);
        }
        roleRepository.save(role);
        user.addRole(role);
        userRepository.save(user);
    }

    @Override
    public void saveAdmin(User user) {
        Role adminRole = roleRepository.findByName(ADMIN_ROLE);
        if (adminRole == null) {
            adminRole = new Role(ADMIN_ROLE);
        }
        roleRepository.save(adminRole);
        user.addRole(adminRole);
        userRepository.save(user);
    }

    @Override
    public void addRole(User user, Role newRole) {
        Role role = roleRepository.findByName(newRole.getName());
        if (role == null) {
            role = new Role(newRole.getName());
            roleRepository.save(role);
        }
        user.addRole(role);
        userRepository.save(user);
    }

    @Override
    public void updateUser(int id, User user) {
        User oldUser = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found"));
        oldUser.setUsername(user.getUsername());
        oldUser.setPassword(user.getPassword());
        userRepository.save(oldUser);
    }

    @Override
    public void deleteUserById(int id) {
        userRepository.deleteById(id);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Unknown user: " + username);
        }

        return new org.springframework.security.core.userdetails.User
                (user.getUsername()
                        , user.getPassword()
                        , ugh(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> ugh(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toSet());
    }


}
