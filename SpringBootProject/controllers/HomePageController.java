package com.hololitt.SpringBootProject.controllers;

import com.hololitt.SpringBootProject.models.LanguageCard;
import com.hololitt.SpringBootProject.models.User;
import com.hololitt.SpringBootProject.services.LanguageCardService;
import com.hololitt.SpringBootProject.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/Home")
@RequiredArgsConstructor
public class HomePageController {
    private final UserService userService;
    private final LanguageCardService languageCardService;

@PostMapping("/add")
    public String add(@ModelAttribute("user") User user){
userService.addUser(user);
return "create";
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

@GetMapping("/searchPrototypes")
    public String searchPrototypes(){
        List<LanguageCard> languageCards = languageCardService.getLanguageCardsByUserId(userService.getUserId());
    Set<LanguageCard> languageCardSet = new HashSet<>(languageCards);

    System.out.println("list size: " + languageCards.size());
    System.out.println("set size: " + languageCardSet.size());

    List<LanguageCard> languageCardList =
            languageCardSet.stream().filter(l -> searchCoincides(l.getWord(), l.getTranslation(), languageCardSet)).toList();

    System.out.println("unique list with words and translations. Size:  " + languageCardList.size());
    return "profile";
}

private boolean searchCoincides(String word, String translation, Set<LanguageCard> languageCards){

   for(LanguageCard languageCard : languageCards){
       if(languageCard.getWord().equals(word) || languageCard.getTranslation().equals(translation)){
           return true;
       }
   }
   return false;
}
}
