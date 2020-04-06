package com.ibm.certificationsserver.service;

import com.ibm.certificationsserver.util.CustomPasswordEncoder;
import com.ibm.certificationsserver.exceptions.NotFoundException;
import com.ibm.certificationsserver.model.User;
import com.ibm.certificationsserver.persistence.UserRepository;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        /*User admin = new User();
        admin.setName("admin");
        admin.setPassword("admin123");
        admin.setRole("ADMIN");
        admin.setUsername("admin");
        admin.setUserId((long) 1);
        userRepository.save(admin);*/
    }


    private User fetchUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw new NotFoundException();
        }
        return user.get();
    }

    public User getUser(Long id) {
        return fetchUser(id);
    }

    public List<User> getUsers() {
        return IteratorUtils.toList(userRepository.findAll().iterator());
    }

    public User createUser(User user) {
        String rawPassword = user.getPassword();
        String encoded = new CustomPasswordEncoder().encode(rawPassword);
        user.setPassword(encoded);
        return userRepository.save(user);
    }

    public User editUser(Long userId, User user) {
        User foundUser = fetchUser(userId);
        foundUser.setName(user.getName());
        return foundUser;

    }
    public Long getIdByUsername(String username)
    {
        Optional<User> userOptional=userRepository.findByUsername(username);
        if(!userOptional.isPresent())
        {
            throw new NotFoundException();
        }
        User foundUser=userOptional.get();
        return foundUser.getUserId();
    }
}
