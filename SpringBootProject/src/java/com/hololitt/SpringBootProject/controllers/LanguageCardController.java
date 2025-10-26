package com.hololitt.SpringBootProject.controllers;

import com.hololitt.SpringBootProject.exceptions.LanguageCardNotFoundException;
import com.hololitt.SpringBootProject.models.LanguageCard;
import com.hololitt.SpringBootProject.models.LanguageCardCreationForm;
import com.hololitt.SpringBootProject.models.LanguageCardEditForm;
import com.hololitt.SpringBootProject.services.LanguageCardCacheService;
import com.hololitt.SpringBootProject.services.LanguageCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.hololitt.SpringBootProject.services.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/Home/WordsTrainer")
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class LanguageCardController {
    private final UserService userservice;
    private final LanguageCardService languageCardService;
    private final LanguageCardCacheService languageCardCacheService;


    @GetMapping("/delete/{id}")
    public String deleteLanguageCard(@PathVariable("id") int id) throws LanguageCardNotFoundException {
        long userId = userservice.getUserId();
        if(languageCardService.findLanguageCardByIdAndUserId(id, userId) != null){
            languageCardService.deleteLanguageCardById(id, userId);
       languageCardService.deleteLanguageCardsCache(userservice.getUserId());     //languageCardCacheService.updateLanguageCardsForUser(userId);
        }else{
            throw new LanguageCardNotFoundException("language card with id " + id + " not exists");
        }
        return "showLanguageCardsByUser";
    }

    @GetMapping("/search")
    public String showOperations(){
        return "LanguageCardOperations";
    }
    @PostMapping("/search/{type}/{word}")
    public String findLanguageCard(Model model, @PathVariable("type") String type, @PathVariable("word") String value){
        LanguageCard foundedLanguageCard = languageCardService.searchLanguageCard(type, value);
        if(foundedLanguageCard != null){
            model.addAttribute("foundedLanguageCard", foundedLanguageCard);
        }else{
            model.addAttribute("nothingFounded",
                    "Language card with value "+value+"not exist in base");
        }
        return "LanguageCardOperations";
    }
    @GetMapping("/editLanguageCard/{id}")
    public String showEditionPage(Model model, @PathVariable("id") int id){
        LanguageCard languageCard = languageCardService.findLanguageCardByIdAndUserId(id, userservice.getUserId());
        if(languageCard == null){
            return "redirect:/Home/WordsTrainer/languageCardNotExist";
        }
        languageCardCacheService.setLanguageCardToEdit(languageCard);
        LanguageCardEditForm languageCardEditForm = new LanguageCardEditForm();
        languageCardEditForm.setWord(languageCard.getWord());
        languageCardEditForm.setTranslation(languageCard.getTranslation());
        languageCardEditForm.setId(languageCard.getId());
        model.addAttribute("languageCardEditForm", languageCardEditForm);
        return "languageCardEdit";
    }
    @GetMapping("/languageCardNotExist")
    public String showNotFoundPage(Model model){
        model.addAttribute("languageCardNotFound", "This language card not exist in base");
        return "languageCardNotFound";
    }
    @PostMapping("/submitEdit")
    public String submitEdit(@ModelAttribute("languageCardEditForm") LanguageCardEditForm languageCardEditForm){
        LanguageCard editedLanguageCard = languageCardCacheService.getLanguageCardToEdit();
        editedLanguageCard.setWord(languageCardEditForm.getWord());
        editedLanguageCard.setTranslation(languageCardEditForm.getTranslation());
        editedLanguageCard.setUserId((int) userservice.getUserId());

        languageCardService.updateLanguageCard(editedLanguageCard, userservice.getUserId());
      //  languageCardCacheService.updateLanguageCardsForUser(userservice.getUserId());
        return "redirect:/Home/WordsTrainer/languageCards";
    }

    @GetMapping("/languageCards")
    public String showLanguageCardsByUser(Model model) {
        List<LanguageCard> languageCards = languageCardService.getLanguageCardsByUserId(userservice.getUserId());
        model.addAttribute("languageCardsAmount", languageCards.size());
        model.addAttribute("languageCardsByUser", languageCards);
        return "showLanguageCardsByUser";
    }

    @PostMapping("/submitCreation")
    public String submitCreation(@ModelAttribute("languageCardCreationForm")
                                 LanguageCardCreationForm languageCardCreationForm, Model model){
        String word = languageCardCreationForm.getWord();
        String translation = languageCardCreationForm.getTranslation();

 word = word.trim();
 translation = translation.trim();

        if(languageCardService.isLanguageCardExists(word, translation)){
            model.addAttribute("languageCardExists", "This language card exists in base");
            return "setLanguageCards";
        }

        languageCardCacheService.addCreatedLanguageCard(languageCardCreationForm, userservice.getUserId());
        model.addAttribute("languageCardCreationForm", new LanguageCardCreationForm());
        model.addAttribute("successfulCreation", "This language card was successful created!");
        model.addAttribute("createdLanguageCards", languageCardCacheService.getCreatedLanguageCards(
                userservice.getUserId()));
        return "setLanguageCards";
    }

    @GetMapping("/creation")
    public String createLanguageCard(Model model){
        LanguageCardCreationForm languageCardCreationForm = new LanguageCardCreationForm();
        model.addAttribute("languageCardCreationForm", languageCardCreationForm);
        return "setLanguageCards";
    }
    @GetMapping("/learnedWordsStats")
    public ResponseEntity<Map<LocalDate, Long>> getLearnedWordsStats() {
       Map<LocalDate, Long> cards = languageCardService.getLearnedWordsStats();

        return ResponseEntity.ok(languageCardService.getLearnedWordsStats());
    }

}
