package com.hololitt.SpringBootProject.controllers;

import com.hololitt.SpringBootProject.DTO.CheckAnswerDTO;
import com.hololitt.SpringBootProject.models.*;
import com.hololitt.SpringBootProject.services.LanguageCardCacheService;
import com.hololitt.SpringBootProject.services.LanguageCardService;
import com.hololitt.SpringBootProject.services.UserService;
import com.hololitt.SpringBootProject.services.WordsTrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/Home/WordsTrainer")
public class WordsTrainerController {
    private final UserService userService;
    private final LanguageCardService languageCardService;
    private LanguageCardContextHolder languageCardContextHolder;
    private final LanguageCardCacheService languageCardCacheService;
    private final WordsTrainerService wordsTrainerService;
    @Autowired
    public WordsTrainerController(LanguageCardService languageCardService,
                                  UserService userService,
                                  LanguageCardCacheService languageCardCacheService,
                                  WordsTrainerService wordsTrainerService){
        this.languageCardService = languageCardService;
        this.userService = userService;
        this.languageCardCacheService = languageCardCacheService;
        this.wordsTrainerService = wordsTrainerService;
    }
    @PostMapping("/submitCreation")
    public String submitCreation(@ModelAttribute("languageCardCreationForm")
                                 LanguageCardCreationForm languageCardCreationForm, Model model){
       wordsTrainerService.createLanguageCard(languageCardCreationForm, userService.getUserId());
        model.addAttribute("languageCardCreationForm", new LanguageCardCreationForm());
        model.addAttribute("successfulCreation", "This language card was successful created!");
        return "setLanguageCards";
    }
    @GetMapping("/creation")
    public String createLanguageCard(Model model){
        LanguageCardCreationForm languageCardCreationForm = new LanguageCardCreationForm();
        model.addAttribute("languageCardCreationForm", languageCardCreationForm);
        return "setLanguageCards";
    }
    @GetMapping
    public String showWordsTrainerPage(){
        Authentication getAuthentication = SecurityContextHolder.getContext().getAuthentication();
        if(getAuthentication.isAuthenticated()){
            return "WordsTrainer";
        }else{
            return "login";
        }
    }
    @GetMapping("/languageCards")
    public String showLanguageCardsByUser(Model model){
        List<LanguageCard> languageCards = languageCardCacheService.getLanguageCardsByUser();
        model.addAttribute("languageCardsAmount", languageCards.size());
        model.addAttribute("languageCardsByUser", languageCards);
        return "showLanguageCardsByUser";
    }
    @GetMapping("/preStart")
    public String preStart(){
        languageCardContextHolder = wordsTrainerService.getLanguageCardContextHolder();
        List<LanguageCard> languageCardsToLearn = languageCardContextHolder.getLanguageCardsToLearn();
        if(languageCardsToLearn == null){
            return "emptyListException";
        }else{
            return "redirect:/Home/WordsTrainer/start";
        }
    }
    @GetMapping("/start")
    public String start(Model model){
        languageCardContextHolder.generateRandomValueAndLanguageCard();

        LanguageCard randomLanguageCard = languageCardContextHolder.getRandomLanguageCard();
        String randomValue = languageCardContextHolder.getRandomValue();

        int correctAnswersCount = wordsTrainerService.calculateCorrectAnswersCount(randomLanguageCard);
        int languageCardsToLearnAmount = languageCardContextHolder.getLanguageCardsToLearn().size();
        int learnedLanguageCardsAmount = languageCardContextHolder.getLearnedLanguageCards().size();

        model.addAttribute("answer", new TranslationModel());
        model.addAttribute("randomValue", randomValue);
        model.addAttribute("languageCardsToLearnAmount", languageCardsToLearnAmount);
        model.addAttribute("learnedLanguageCardsAmount", learnedLanguageCardsAmount);
        model.addAttribute("correctAnswersCount", correctAnswersCount);
        return "checkTranslation";
    }
    @PostMapping("/checkAnswer")
    public String checkAnswer(@ModelAttribute("answer") TranslationModel translationModel, Model model){
        CheckAnswerDTO result = wordsTrainerService.checkAnswer(translationModel);
        if (!result.isCorrect()) {
            model.addAttribute("correctAnswer", result.getCorrectAnswer());
            model.addAttribute("incorrectAnswerMessage", "Incorrect!");
            return "mistakePage";
        }

        if (result.getCorrectAnswer() == null) {
            return "redirect:/Home/WordsTrainer/finish";
        }

        return "redirect:/Home/WordsTrainer/start";
    }
    @GetMapping("/finish")
    public String finish(Model model){
        List<LanguageCard> languageCardList = languageCardContextHolder.getLearnedLanguageCards();
        model.addAttribute("learnedLanguageCards", languageCardList);

        languageCardService.saveLanguageCardList(languageCardList);
        languageCardCacheService.setLastLearnedLanguageCards(languageCardList);
        languageCardCacheService.updateLanguageCardsForUser(userService.getUserId());
        return "finish";
    }
    @GetMapping("/repeat")
    public String repeatLanguageCards(){
        List<LanguageCard> languageCards = languageCardCacheService.getLanguageCardsByUser();
  return repeatLanguageCardsOrReturnException(languageCards);
    }
    @GetMapping("/repeatMostDifficult")
    public String repeatMostDifficult(){
    List<LanguageCard> languageCards = wordsTrainerService.getMostDifficultLanguageCardsToLearn();
          return repeatLanguageCardsOrReturnException(languageCards);
    }
    @GetMapping("/repeatRecommended")
    public String startRecommendedTraining(){
        List<LanguageCard> languageCards = wordsTrainerService.getRecommendedLanguageCardsToLearn();
        return repeatLanguageCardsOrReturnException(languageCards);
    }
    @GetMapping("/repeatLastLearned")
    public String repeatLastLearned(){
        List<LanguageCard> languageCards = languageCardCacheService.getLastLearnedLanguageCards();
      return repeatLanguageCardsOrReturnException(languageCards);
    }
    private String repeatLanguageCardsOrReturnException(List<LanguageCard> languageCards) {
        if (wordsTrainerService.isListValid(languageCards)) {
            wordsTrainerService.setLanguageCardsToLearn(languageCards);
            return "redirect:/Home/WordsTrainer/preStart";
        }
        return "emptyListException";
    }
    }