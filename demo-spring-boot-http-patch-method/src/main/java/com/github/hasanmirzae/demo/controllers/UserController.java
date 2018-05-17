package com.github.hasanmirzae.demo.controllers;

import com.github.hasanmirzae.demo.exceptions.UserNotFoundException;
import com.github.hasanmirzae.demo.models.User;
import com.github.hasanmirzae.demo.repository.UserRepository;
import com.github.hasanmirzae.demo.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class UserController {

    private UserRepository repository;
    private UserService userService;

    public UserController(UserRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @GetMapping("/users")
    public Collection<User> getUsers(){
        return repository.findAll();
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user){
        return repository.save(user);
    }

    @PutMapping("/users")
    public User replace(@RequestBody User user){
        if (user.getId() == null)
            throw new IllegalArgumentException("User id is undefined");
        return repository.save(user);
    }

    @PatchMapping("/users")
    public User update(@RequestBody User user) throws UserNotFoundException {
        return userService.patchUser(user);
    }

}
