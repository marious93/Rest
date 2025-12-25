package ru.kata.spring.boot_security.demo.service;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.enity.Role;
import ru.kata.spring.boot_security.demo.enity.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomUserServiceImpl implements CustomUserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final RoleRepository roleRepository;


    public CustomUserServiceImpl(UserRepository userRepository, RoleService roleService, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleService = roleService;
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
    public void saveUser(User user, List<Integer> roleIds) {
        Set<Role> roles = roleIds.stream()
                .map(id -> roleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Role not found")))
                .collect(Collectors.toSet());
        user.setRoles(roles);
        userRepository.save(user);
    }

    @Override
    public void updateUser(int id, User user, List<Integer> roleIds) {
        User oldUser = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found"));
        oldUser.setUsername(user.getUsername());
        oldUser.setPassword(user.getPassword());
        oldUser.setLastName(user.getLastName());
        oldUser.setFirstName(user.getFirstName());
        oldUser.setAge(user.getAge());
        Set<Role> roles = roleIds.stream()
                .map(i -> roleRepository.findById(i).orElseThrow(() -> new EntityNotFoundException("Role not found")))
                .collect(Collectors.toSet());
        oldUser.setRoles(roles);
        userRepository.save(oldUser);
    }

    @Override
    public void deleteUserById(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public int getUserIdByUsername(String username) {
        return userRepository.getUserIdByUsername(username);
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
                        , convertMapToSet(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> convertMapToSet(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toSet());
    }

}
