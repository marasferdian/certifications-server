package com.ibm.certificationsserver.service;

import com.ibm.certificationsserver.util.CustomPasswordEncoder;
import com.ibm.certificationsserver.exceptions.NotFoundException;
import com.ibm.certificationsserver.model.User;
import com.ibm.certificationsserver.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository=userRepository;
    }

    public User getUser(Long id) {
        return userRepository.getUser(id);
    }

    public List<User> getUsers() {
        return userRepository.getUsers();
    }

    public User createUser(User user) {
        return userRepository.saveUser(user);
    }

    public User update(User user) {
        return userRepository.saveUser(user);
    }

    public Long getIdByUsername(String username)
    {
        User user=userRepository.findByUsername(username);
        if(user==null)
            throw new NotFoundException();
        return user.getId();

    }
}
