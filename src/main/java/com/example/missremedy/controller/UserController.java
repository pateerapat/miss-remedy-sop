package com.example.missremedy.controller;

import com.example.missremedy.pojo.User;
import com.example.missremedy.repository.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/service/user/{name}", method = RequestMethod.GET)
    public User getUserByName(@PathVariable("name") String name) {
        return userService.getUserByUsername(name);
    }

    @RequestMapping(value = "/service/updateUser/", method = RequestMethod.POST)
    public User updateUser(@RequestBody MultiValueMap<String, String> map) {
        Map<String, String> d = map.toSingleValueMap();
        User user = userService.getUserByUsernameWithId(d.get("3"));
        user.setFirstName(d.get("1"));
        user.setLastName(d.get("2"));
        userService.editUser(user);
        return user;
    }
}
