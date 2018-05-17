package com.github.hasanmirzae.demo.repository;

import com.github.hasanmirzae.demo.models.User;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepository {

    private Long idGenerator = 0L;

    private Map<Long,User> repo = new HashMap();


    public User save(User user) {
        if (user.getId() == null){
            user.setId(++idGenerator);
        }
        repo.put(user.getId(),user);
        return user;
    }


    public User findById(Long id) {
        return repo.get(id);
    }



    public Collection<User> findAll() {
        return repo.values();
    }



    public void deleteById(Long id) {
        repo.remove(id);
    }

    public void delete(User user) {
        repo.remove(user.getId());
    }

}
