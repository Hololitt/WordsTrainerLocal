package com.hololitt.SpringBootProject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/Home")
public class LogoutController {
    @GetMapping("/logout")
    public String logout(){
        return "redirect:/Home/login?logout";
    }
}
