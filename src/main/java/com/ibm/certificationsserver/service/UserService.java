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

        /*
        User admin = new User();
        admin.setName("admin");
        CustomPasswordEncoder enc = new CustomPasswordEncoder();
        admin.setPassword(enc.encode("admin123"));
        admin.setRole("ADMIN");
        admin.setUsername("admin");
        admin.setId(null);  //null because we have ID as Long object
        userRepository.saveUser(admin);
        */
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
        if(user==null) {
            throw new NotFoundException();
        }
        return user.getId();

    }

    public void deleteUser(long id) {
        userRepository.deleteUser(id);
    }

    public void updateUserPassword(long id, String pass) {
        userRepository.updateUserPassword(id,pass);
    }
}
