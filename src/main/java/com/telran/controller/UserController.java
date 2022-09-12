package com.telran.controller;


import com.telran.model.User;
import com.telran.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class UserController {

//    @RequestMapping(method = RequestMethod.POST)
    private Map<String, User> users = new HashMap<>();

    @Value("${server.port}")
    private int port;


    @Autowired
    private UserService userService;


    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        user.setId(UUID.randomUUID().toString());

        users.put(user.getId(), user);
        return user;
    }


    @GetMapping("/users")
    public List<User> findAllUsers() {
        return users.values().stream().toList();
    }

    // http://localhost:8080/users/123

    @GetMapping("/users/{userId}")
    public User findByUserId(@PathVariable("userId") String externalUserId) {
        User user = users.get(externalUserId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT);
        }

        return user;
    }


    // DELETE
    // http://localhost:8080/users?last-name=Smith
    @DeleteMapping("/users")
    public void deleteAllUsers(@RequestParam(value = "last-name", required = false) String lastName) {
        if (lastName == null) {
            users.clear();
            return;
        }

        users = users.entrySet()
                .stream()
                .filter(user -> !user.getValue().getLastName().equals(lastName))
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue
                        ));
    }


}
