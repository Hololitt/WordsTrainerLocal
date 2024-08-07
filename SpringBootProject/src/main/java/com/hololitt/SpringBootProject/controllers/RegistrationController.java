package com.hololitt.SpringBootProject.controllers;

import com.hololitt.SpringBootProject.models.RegistrationForm;
import com.hololitt.SpringBootProject.models.User;
import com.hololitt.SpringBootProject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/Home")
public class RegistrationController {
    private final UserService userService;
    @Autowired
    public RegistrationController(UserService userService){
        this.userService = userService;
    }
    @GetMapping("/registration")
    public String showRegistrationForm(Model model){
        model.addAttribute("registrationForm", new RegistrationForm());
        return "registration";
    }
    @PostMapping("/registration")
    public String processRegistration(@ModelAttribute("registrationForm") User user) {
        userService.addUser(user);
        return "redirect:/Home/login";
    }
}
