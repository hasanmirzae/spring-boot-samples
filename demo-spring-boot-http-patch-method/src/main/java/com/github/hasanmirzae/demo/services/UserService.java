package com.github.hasanmirzae.demo.services;

import com.github.hasanmirzae.demo.exceptions.UserNotFoundException;
import com.github.hasanmirzae.demo.models.User;
import com.github.hasanmirzae.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User patchUser(User user) throws UserNotFoundException {
        User toBeUpdated = repository.findById(user.getId());
        if (toBeUpdated == null)
            throw new UserNotFoundException("User does not exist");
        toBeUpdated.patchBy(user);
        return repository.save(toBeUpdated);
    }
}
