package com.hololitt.SpringBootProject.repositories;

import com.hololitt.SpringBootProject.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findAll();
User findByName(String username);
    boolean existsByName(String username);
}
