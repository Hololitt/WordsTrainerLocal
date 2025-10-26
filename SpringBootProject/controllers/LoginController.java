package com.hololitt.SpringBootProject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.hololitt.SpringBootProject.models.LoginForm;
@Controller
@RequestMapping("/Home")
public class LoginController {

    @GetMapping("/login")
    public String login(Model model){
        LoginForm loginForm = new LoginForm();
        model.addAttribute("loginForm", loginForm);
        return "login";
    }
}
