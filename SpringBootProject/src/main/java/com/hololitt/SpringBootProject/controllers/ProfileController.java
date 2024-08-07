package com.hololitt.SpringBootProject.controllers;

import com.hololitt.SpringBootProject.Config.CustomUserDetails;
import com.hololitt.SpringBootProject.services.LanguageCardService;
import com.hololitt.SpringBootProject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller
@RequestMapping("/Home")
public class ProfileController {
    private final UserService userService;
    private final LanguageCardService languageCardService;
    @Autowired
    public ProfileController(UserService userService, LanguageCardService languageCardService){
        this.userService = userService;
        this.languageCardService = languageCardService;
    }
    @GetMapping("/{username}")
    public String showProfile(@PathVariable("username") String username, Model model){
        CustomUserDetails currentUser = userService.getCurrentUser();

        if(!Objects.equals(username, currentUser.getUsername())){
            return "error";
        }
        if(userService.isUserExist(username)){
            model.addAttribute("username", username);
            long userId = currentUser.getId();
            model.addAttribute("countLanguageCards", languageCardService.getCountLanguageCards(userId));
        }else{
            return "error";
        }
return "profile";
    }
    @GetMapping("/showProfile")
    public String show(){
        CustomUserDetails currentUser = userService.getCurrentUser();
        return "redirect:/Home/"+currentUser.getUsername();
    }
}
