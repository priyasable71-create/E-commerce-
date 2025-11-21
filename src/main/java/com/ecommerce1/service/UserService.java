package com.ecommerce1.service;

import com.ecommerce1.model.User;
import com.ecommerce1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean validateUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        return user != null && user.getPassword().equals(password);
    }
}
