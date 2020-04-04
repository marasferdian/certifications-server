package com.ibm.certificationsserver.persistence;

import com.ibm.certificationsserver.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findById(Long userId);

    Optional<User> findByUsername(String username);
}
