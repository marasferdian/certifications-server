package com.ibm.certificationsserver.persistence;

import com.ibm.certificationsserver.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface UserRepository{

    User findByUsername(String username);

    User getUser(Long id);

    List<User> getUsers();

    User saveUser(User user);
}
