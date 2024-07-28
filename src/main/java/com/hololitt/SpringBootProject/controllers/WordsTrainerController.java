package com.hololitt.SpringBootProject.controllers;

import com.hololitt.SpringBootProject.models.*;
import com.hololitt.SpringBootProject.services.LanguageCardCacheService;
import com.hololitt.SpringBootProject.services.LanguageCardService;
import com.hololitt.SpringBootProject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/Home/WordsTrainer")
public class WordsTrainerController {
    private final UserService userService;
    private final LanguageCardService languageCardService;
    private final LanguageCardContextHolder languageCardContextHolder = new LanguageCardContextHolder();
    private List<LanguageCard> languageCardsToLearn;
    private final LanguageCardCacheService languageCardCacheService;
    @Autowired
    public WordsTrainerController(LanguageCardService languageCardService, UserService userService,
                                  LanguageCardCacheService languageCardCacheService){
        this.languageCardService = languageCardService;
        this.userService = userService;
        this.languageCardCacheService = languageCardCacheService;
    }

    @PostMapping("/submitCreation")
    public String submitCreation(@ModelAttribute("languageCardCreationForm")
                                 LanguageCardCreationForm languageCardCreationForm, Model model){
        languageCardContextHolder.addLanguageCardToLearn(
                new LanguageCard(languageCardCreationForm.getWord(), languageCardCreationForm.getTranslation()));
        // Очистка полей ввода
        LanguageCardCreationForm emptyForm = new LanguageCardCreationForm();
        model.addAttribute("languageCardCreationForm", emptyForm);

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
        languageCardContextHolder.cleanUpContext();
        languageCardContextHolder.setLanguageCardsToLearn(languageCardsToLearn);
        return "redirect:/Home/WordsTrainer/start";
    }
    @GetMapping("/start")
    public String start(Model model){
        languageCardContextHolder.generateRandomValueAndLanguageCard();
        LanguageCard randomLanguageCard = languageCardContextHolder.getRandomLanguageCard();
        String randomValue = languageCardContextHolder.getRandomValue();
        if(randomLanguageCard.getUserId() == 0){
            randomLanguageCard.setUserId(userService.getUserId());
        }
        model.addAttribute("randomValue", randomValue);
        TranslationModel translationModel = new TranslationModel();
        model.addAttribute("answer", translationModel);
        int correctAnswersCount;
        if(languageCardContextHolder.getCorrectAnswers().isEmpty()){
            correctAnswersCount = 0;
        }else{
            correctAnswersCount = languageCardContextHolder.getCorrectAnswers(randomLanguageCard);
        }
        int languageCardsToLearnAmount = languageCardContextHolder.getLanguageCardsToLearn().size();
        int learnedLanguageCardsAmount = languageCardContextHolder.getLearnedLanguageCards().size();

        model.addAttribute("languageCardsToLearnAmount", languageCardsToLearnAmount);
        model.addAttribute("learnedLanguageCardsAmount", learnedLanguageCardsAmount);

        model.addAttribute("correctAnswersCount", correctAnswersCount);
        return "checkTranslation";
    }
    @PostMapping("/checkAnswer")
    public String checkAnswer(@ModelAttribute("answer") TranslationModel translationModel, Model model){
        String answer = translationModel.getAnswer();
        LanguageCard randomLanguageCard = languageCardContextHolder.getRandomLanguageCard();
        String randomValue = languageCardContextHolder.getRandomValue();
        String correctAnswer = languageCardContextHolder.getCorrectAnswer(randomValue, randomLanguageCard);
        if(!languageCardContextHolder.isCorrectAnswer(randomLanguageCard, answer, correctAnswer)){
            model.addAttribute("incorrectAnswerMessage", "Incorrect!");
            model.addAttribute("correctAnswer", correctAnswer);
            randomLanguageCard.incrementMistakesCount();
            return "mistakePage";
        }else{
            model.addAttribute("correctAnswerMessage", "Correct!");
        }
        if(languageCardContextHolder.getCorrectAnswers(randomLanguageCard) == 3){
            randomLanguageCard.incrementRepeatCount();
            languageCardContextHolder.addLearnedLanguageCard(randomLanguageCard);
            languageCardContextHolder.removeFromCorrectAnswers(randomLanguageCard);
            languageCardContextHolder.removeFromLanguageCardsToLearn(randomLanguageCard);
        }
        if(languageCardContextHolder.getLanguageCardsToLearn().isEmpty()){
            return "redirect:/Home/WordsTrainer/finish";
        }
        return "redirect:/Home/WordsTrainer/start";
    }
    @GetMapping("/finish")
    public String finish(Model model){
        List<LanguageCard> languageCardList = languageCardContextHolder.getLearnedLanguageCards();
        model.addAttribute("learnedLanguageCards", languageCardList);
        languageCardService.saveLanguageCardList(languageCardList);
        languageCardCacheService.updateLanguageCardsForUser(userService.getUserId());
        return "finish";
    }
    @GetMapping("/repeat")
    public String repeatLanguageCards(){
        languageCardsToLearn = languageCardCacheService.getLanguageCardsByUser();
        return "redirect:/Home/WordsTrainer/preStart";
    }
    @GetMapping("/repeatMostDifficult")
    public String repeatMostDifficult(){
        List<LanguageCard> languageCards = languageCardCacheService.getLanguageCardsByUser();
        languageCards.sort(Comparator.comparingInt(LanguageCard::getMistakesCount).reversed());

        List<LanguageCard> highestMistakesCount = languageCards.stream()
                .limit(5)
                .collect(Collectors.toList());
        if(languageCards.isEmpty()){
            return "emptyListException";
        }
        languageCardsToLearn = highestMistakesCount;
        return "redirect:/Home/WordsTrainer/preStart";
    }
    @GetMapping("/repeatRecommended")
    public String startRecommendedTraining(){
        List<LanguageCard> languageCards = languageCardCacheService.getLanguageCardsByUser();

        languageCardsToLearn = languageCards.stream()
                .sorted(Comparator.comparingInt(LanguageCard::getRepeatCount)).limit(5).collect(Collectors.toList());

        return "redirect:/Home/WordsTrainer/preStart";
    }

}