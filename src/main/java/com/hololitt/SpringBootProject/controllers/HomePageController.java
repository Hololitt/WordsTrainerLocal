package com.hololitt.SpringBootProject.controllers;

import com.hololitt.SpringBootProject.models.LanguageCard;
import com.hololitt.SpringBootProject.models.User;
import com.hololitt.SpringBootProject.services.LanguageCardService;
import com.hololitt.SpringBootProject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/Home")
public class HomePageController {
    private final UserService userService;
    private final LanguageCardService languageCardService;
    @Autowired
public HomePageController(UserService userService, LanguageCardService languageCardService){
    this.userService = userService;
    this.languageCardService = languageCardService;
}
@PostMapping("/add")
    public String add(@ModelAttribute("user") User user){
userService.addUser(user);
return "create";
}
@GetMapping("/create")
    public String create(Model model){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if(!authentication.isAuthenticated()){
        return "redirect:/Home/login";
    }
        User user = new User();
    model.addAttribute("user", user);
    return "create";
}
@GetMapping("/delete")
public String delete(Model model){
        User userForDeletion = new User();
        model.addAttribute("userForDeletion", userForDeletion);
        return "delete";
}
@PostMapping("/submitDeletion")
public String submitDeletion(@ModelAttribute("userForDeletion") User userForDeletion){
        userService.deleteUser(userForDeletion);
        return "delete";
}
@GetMapping("/users")
    public String show(Model model){
    List<User> userList = userService.getAllUsers();
   model.addAttribute(userList);
        return "users";
}
@GetMapping("/languageCards")
    public String showLanguageCards(Model model){
        List<LanguageCard> languageCardList = languageCardService.getLanguageCardList();
        model.addAttribute("languageCardList", languageCardList);
        return "showLanguageCards";
}
}
