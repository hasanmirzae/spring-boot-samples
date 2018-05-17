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
        User updated = repository.findById(user.getId());
        if (updated == null)
            throw new UserNotFoundException("User does not exits");
        if (user.getUpdatingFields() == null || user.getUpdatingFields().isEmpty())
            throw new IllegalArgumentException("Updating field names are not defined");
        updated.patchBy(user);
        return repository.save(updated);
    }
}
