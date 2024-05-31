package com.mobiauto.service;

import com.mobiauto.data.User;
import com.mobiauto.repository.UserRepository;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
@Serdeable
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
