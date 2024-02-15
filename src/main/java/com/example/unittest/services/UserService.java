package com.example.unittest.services;

import com.example.unittest.entities.User;
import com.example.unittest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    public List<User> getAll() {
            return userRepository.findAll();
    }

    public User getOne(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else return null;
    }

    public User changeSurname(long id, User user) {
        if (userRepository.existsById(id)) {
            Optional<User> changedSurnameOfUser = userRepository.findById(id);
            changedSurnameOfUser.get().setSurname(user.getSurname());
            return userRepository.saveAndFlush(changedSurnameOfUser.get());
        } else return null;
    }

    public Boolean deleteOne(long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return !userRepository.existsById(id);
        } else return null;
    }
}
