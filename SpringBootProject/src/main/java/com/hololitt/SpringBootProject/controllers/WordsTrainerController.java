package com.hololitt.SpringBootProject.controllers;

import com.hololitt.SpringBootProject.DTO.CheckAnswerDTO;
import com.hololitt.SpringBootProject.models.*;
import com.hololitt.SpringBootProject.services.*;
import com.hololitt.SpringBootProject.validators.LanguageCardValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/Home/WordsTrainer")
public class WordsTrainerController {
    private final UserService userService;
    private final LanguageCardService languageCardService;
    private final LanguageCardContextHolder languageCardContextHolder;
    private final LanguageCardCacheService languageCardCacheService;
    private final WordsTrainerService wordsTrainerService;
    private final WordsTrainerSettingsService wordsTrainerSettingsService;
    private final LanguageCardValidator languageCardValidator;
    @Autowired
    public WordsTrainerController(LanguageCardService languageCardService,
                                  UserService userService,
                                  LanguageCardCacheService languageCardCacheService,
                                  WordsTrainerService wordsTrainerService,
                                  LanguageCardContextHolder languageCardContextHolder,
                                  WordsTrainerSettingsService wordsTrainerSettingsService,
                                  LanguageCardValidator languageCardValidator
                                  ){
        this.languageCardService = languageCardService;
        this.userService = userService;
        this.languageCardCacheService = languageCardCacheService;
        this.wordsTrainerService = wordsTrainerService;
        this.languageCardContextHolder = languageCardContextHolder;
        this.wordsTrainerSettingsService = wordsTrainerSettingsService;
        this.languageCardValidator = languageCardValidator;
    }
    @GetMapping("/cancelTraining")
    public String cancelTraining(){
        languageCardContextHolder.cleanUpContext();
        languageCardCacheService.deleteCreatedLanguageCards();
        return "WordsTrainer";
    }
    @PostMapping("/submitCreation")
    public String submitCreation(@ModelAttribute("languageCardCreationForm")
                                 LanguageCardCreationForm languageCardCreationForm, Model model){
       languageCardCacheService.addCreatedLanguageCard(languageCardCreationForm, userService.getUserId());
        model.addAttribute("languageCardCreationForm", new LanguageCardCreationForm());
        model.addAttribute("successfulCreation", "This language card was successful created!");
        model.addAttribute("createdLanguageCards", languageCardCacheService.getCreatedLanguageCards());
        return "setLanguageCards";
    }
    @GetMapping("/creation")
    public String createLanguageCard(Model model){
        LanguageCardCreationForm languageCardCreationForm = new LanguageCardCreationForm();
        model.addAttribute("languageCardCreationForm", languageCardCreationForm);
        return "setLanguageCards";
    }
    @GetMapping("/prepareLanguageCards")
    public String prepareLanguageCard(){
        List<LanguageCard> languageCardsToLearn = languageCardCacheService.getCreatedLanguageCards();
      return repeatLanguageCardsOrReturnException(languageCardsToLearn);
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
        List<LanguageCard> languageCardsToLearn = languageCardContextHolder.getLanguageCardsToLearn();
        if(languageCardsToLearn == null){
            return "emptyListException";
        }else{
            return "redirect:/Home/WordsTrainer/start";
        }
    }
    @GetMapping("/start")
    public String start(Model model){
        wordsTrainerService.setTrainingType();

        LanguageCard randomLanguageCard = languageCardContextHolder.getRandomLanguageCard();
        String randomValue = languageCardContextHolder.getRandomValue();

        int correctAnswersCount = wordsTrainerService.calculateCorrectAnswersCount(randomLanguageCard);
        int languageCardsToLearnAmount = languageCardContextHolder.getLanguageCardsToLearn().size();
        int learnedLanguageCardsAmount = languageCardContextHolder.getLearnedLanguageCards().size();
        int countCorrectAnswersToFinish = wordsTrainerSettingsService.getSettingsForUser(userService.getUserId())
                .getCorrectAnswersCountToFinish();

        model.addAttribute("correctAnswersCountToFinish", countCorrectAnswersToFinish);
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
        Map<LanguageCard, Integer> mistakesDuringTraining = languageCardContextHolder.getAllMistakesDuringTraining();

        model.addAttribute("learnedLanguageCards", languageCardList);
        model.addAttribute("mistakesDuringTraining", mistakesDuringTraining);

        languageCardService.saveLanguageCardList(languageCardList);
        languageCardCacheService.updateLanguageCardsForUser(userService.getUserId());
        languageCardCacheService.setLastLearnedLanguageCards(languageCardList);
        languageCardCacheService.deleteCreatedLanguageCards();
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
    @PostMapping("/submitSettings")
    public String submitSettings(@ModelAttribute("wordsTrainerSettings") WordsTrainerSettings wordsTrainerSettings,
                                 Model model){
        wordsTrainerSettings.setUserId(userService.getUserId());
wordsTrainerSettingsService.setAllSettings(wordsTrainerSettings);
model.addAttribute("successfulSetting", "Your settings was successful saved!");
return "wordsTrainerSettings";
    }
    @GetMapping("/settings")
    public String showSettings(Model model){
        model.addAttribute("wordsTrainerSettings", new WordsTrainerSettings());
        return "wordsTrainerSettings";
    }
        private String repeatLanguageCardsOrReturnException(List<LanguageCard> languageCards) {
            if (languageCardValidator.isLanguageCardListValid(languageCards)) {
                languageCardContextHolder.cleanUpContext();
               languageCardContextHolder.setLanguageCardsToLearn(languageCards);
               languageCardContextHolder.setContext();
                return "redirect:/Home/WordsTrainer/preStart";
            }
            return "emptyListException";
        }
    }