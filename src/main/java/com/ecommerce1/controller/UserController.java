package com.ecommerce1.controller;

import com.ecommerce1.model.User;
import com.ecommerce1.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(String email, String password, HttpSession session, Model model) {
        User user = userService.findByEmail(email);
        if(user != null && user.getPassword().equals(password)) {
            session.setAttribute("user", user);
            return "redirect:/products";
        } else {
            model.addAttribute("error", "Invalid email or password!");
            return "login";
        }
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(User user) {
        if(userService.findByEmail(user.getEmail()) == null) {
            userService.registerUser(user);
        }
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
