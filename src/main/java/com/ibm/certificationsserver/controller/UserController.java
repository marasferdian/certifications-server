package com.ibm.certificationsserver.controller;

import com.ibm.certificationsserver.exceptions.NotAllowedException;
import com.ibm.certificationsserver.model.User;
import com.ibm.certificationsserver.service.UserService;
import com.ibm.certificationsserver.util.CustomPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    private static List<String> getAuthorityList(Authentication authentication) {
        return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    }

    public  static boolean hasAuthority(Authentication authentication, String authorityName) {
        return getAuthorityList(authentication).contains(authorityName);
    }

    @GetMapping("/user")
    public ResponseEntity getCurrentUser(Authentication authentication)
    {
        UserService service=this.userService;
        String currentUserName=authentication.getName();
        User user=service.getUser(service.getIdByUsername(currentUserName));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity getUsers(Authentication authentication) {
        if (hasAuthority(authentication, "ADMIN")) {
            List<User> userList = userService.getUsers();
            return new ResponseEntity<>(userList, HttpStatus.OK);
        }
        else {
            throw new NotAllowedException();
        }
    }

    @PostMapping("/register")
    public ResponseEntity createUser(@RequestBody User user) {
        user.setId(null);
        String rawPassword = user.getPassword();
        String encoded = new CustomPasswordEncoder().encode(rawPassword);
        user.setPassword(encoded);
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.OK);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity deleteUser(@PathVariable("id") long id){

        userService.deleteUser(id);
        return new ResponseEntity<>(null,HttpStatus.OK);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity updateUserPassword(@PathVariable("id") long id,@RequestParam("password") String pass){

        userService.updateUserPassword(id,pass);
        return new ResponseEntity<>(null,HttpStatus.OK);
    }
}
