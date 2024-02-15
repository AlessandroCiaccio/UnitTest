package com.example.unittest.controllers;

import com.example.unittest.entities.User;
import com.example.unittest.services.UserService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(path = "/create/one")
    public @ResponseBody User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping(path = "/get/all")
    public @ResponseBody List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping(path = "/get/one/{id}")
    public @ResponseBody User getOne(@PathVariable long id) {
        return userService.getOne(id);
    }

    @PatchMapping(path = "/change/surname/{id}")
    public @ResponseBody User changeSurname(@PathVariable long id, @RequestBody User user) {
        return userService.changeSurname(id, user);
    }

    @DeleteMapping("/delete/one/{id}")
    public @ResponseBody Boolean deleteOne(@PathVariable long id) {
        return userService.deleteOne(id);
    }
}
