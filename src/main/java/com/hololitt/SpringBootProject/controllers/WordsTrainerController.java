package com.hololitt.SpringBootProject.controllers;

import com.hololitt.SpringBootProject.DTO.CheckAnswerDTO;
import com.hololitt.SpringBootProject.DTO.TrainingContextDTO;
import com.hololitt.SpringBootProject.models.*;
import com.hololitt.SpringBootProject.services.*;
import com.hololitt.SpringBootProject.validators.LanguageCardValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@SuppressWarnings("unused")
@RequestMapping("/Home/WordsTrainer")
public class WordsTrainerController {
    private final UserService userService;
    private final LanguageCardService languageCardService;
    private final LanguageCardContextHolder languageCardContextHolder;
    private final LanguageCardCacheService languageCardCacheService;
    private final WordsTrainerService wordsTrainerService;
    private final WordsTrainerSettingsService wordsTrainerSettingsService;
    private final LanguageCardValidator languageCardValidator;
    private final FlashCardService flashCardService;
    @Autowired
    public WordsTrainerController(LanguageCardService languageCardService,
                                  UserService userService,
                                  LanguageCardCacheService languageCardCacheService,
                                  WordsTrainerService wordsTrainerService,
                                  LanguageCardContextHolder languageCardContextHolder,
                                  WordsTrainerSettingsService wordsTrainerSettingsService,
                                  LanguageCardValidator languageCardValidator, FlashCardService flashCardService
                                  ){
        this.languageCardService = languageCardService;
        this.userService = userService;
        this.languageCardCacheService = languageCardCacheService;
        this.wordsTrainerService = wordsTrainerService;
        this.languageCardContextHolder = languageCardContextHolder;
        this.wordsTrainerSettingsService = wordsTrainerSettingsService;
        this.languageCardValidator = languageCardValidator;
        this.flashCardService = flashCardService;
    }
    @GetMapping("/cancelTraining")
    public String cancelTraining(){
        languageCardContextHolder.cleanUpContext();
        languageCardCacheService.deleteCreatedLanguageCards();
        languageCardCacheService.deleteSelectedLanguageCardsToRepeat();
        return "redirect:/Home/WordsTrainer";
    }
    @PostMapping("/submitCreation")
    public String submitCreation(@ModelAttribute("languageCardCreationForm")
                                 LanguageCardCreationForm languageCardCreationForm, Model model){
        String word = languageCardCreationForm.getWord();
        String translation = languageCardCreationForm.getTranslation();

        if(languageCardService.isLanguageCardExists(word, translation)){
            model.addAttribute("languageCardExists", "This language card exists in base");
            return "setLanguageCards";
        }

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
    public String showLanguageCardsByUser(Model model) {
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

        TrainingContextDTO trainingDTO = createTrainingDTO(randomLanguageCard);
        List<LanguageCard> languageCards = languageCardContextHolder.getLanguageCardsToLearn();
        model.addAttribute("trainingProgress", wordsTrainerService.calculateProgressPercent(languageCards));
        model.addAttribute("trainingData", trainingDTO);
        model.addAttribute("answer", new TranslationModel());
        return "checkTranslation";
    }
    private TrainingContextDTO createTrainingDTO(LanguageCard card) {
      return TrainingContextDTO.builder()
                .randomValue(languageCardContextHolder.getRandomValue())
                .correctAnswersCount(wordsTrainerService.calculateCorrectAnswersCount(card))
                .languageCardsToLearnAmount(languageCardContextHolder.getLanguageCardsToLearn().size())
                .learnedLanguageCardsAmount(languageCardContextHolder.getLearnedLanguageCards().size())
                .correctAnswersCountToFinish(wordsTrainerSettingsService.getSettingsForUser(userService.getUserId())
                        .getCorrectAnswersCountToFinish())
                .build();
    }
    @PostMapping("/checkAnswer")
    public String checkAnswer(@ModelAttribute("answer") TranslationModel translationModel, Model model){
        CheckAnswerDTO result = wordsTrainerService.checkAnswer(translationModel);

        String flashTrainingType = wordsTrainerSettingsService.getSettingsForUser(userService.getUserId())
                .getFlashCardsTrainingVariety();
        if(result.isRepeatFlashCardsTraining()){
            return defineFlashCardsTrainingType(flashTrainingType);
        }

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

        languageCardCacheService.addLastLearnedLanguageCards(languageCardList, userService.getUserId());
        languageCardService.saveLanguageCardList(languageCardList);
        languageCardCacheService.updateLanguageCardsForUser(userService.getUserId());
        languageCardCacheService.deleteCreatedLanguageCards();
        return "finish";
    }
    @GetMapping("/repeat")
    public String repeatLanguageCards(){
        List<LanguageCard> languageCards = languageCardService.getLanguageCardsByUserId(userService.getUserId());
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
    @GetMapping("/repeatRandom")
    public String repeatRandomLanguageCards(){
        List<LanguageCard> languageCards = wordsTrainerService.getRandomLanguageCardsToLearn();
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
        LanguageCard languageCard = languageCardService.findLanguageCardByIdAndUserId(id, userService.getUserId());
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
 public String notFoundPage(Model model){
     model.addAttribute("languageCardNotFound", "This language card not exist in base");
     return "languageCardNotFound";
 }
 @PostMapping("/submitEdit")
 public String submitEdit(@ModelAttribute("languageCardEditForm") LanguageCardEditForm languageCardEditForm){
        LanguageCard editedLanguageCard = languageCardCacheService.getLanguageCardToEdit();
       editedLanguageCard.setWord(languageCardEditForm.getWord());
       editedLanguageCard.setTranslation(languageCardEditForm.getTranslation());
       editedLanguageCard.setUserId((int) userService.getUserId());
       languageCardService.updateLanguageCard(editedLanguageCard);
     languageCardCacheService.updateLanguageCardsForUser(userService.getUserId());
       return "redirect:/Home/WordsTrainer/languageCards";
 }
        private String repeatLanguageCardsOrReturnException(List<LanguageCard> languageCards) {
        long userId = userService.getUserId();
        WordsTrainerSettings wordsTrainerSettings = wordsTrainerSettingsService.getSettingsForUser(userId);
            if (languageCardValidator.isLanguageCardListValid(languageCards)) {
                  languageCardContextHolder.cleanUpContext();
                languageCardContextHolder.setLanguageCardsToLearn(languageCards);
                languageCardContextHolder.setContext();
                return defineFlashCardsTrainingType(wordsTrainerSettings.getFlashCardsTrainingVariety());
            }
            return "emptyListException";
        }
        private String defineFlashCardsTrainingType(String flashCardsTrainingType){
        String baseURL = "redirect:/Home/WordsTrainer/";

       return switch(flashCardsTrainingType){
           case "noTraining" -> baseURL + "start";
           case "chooseAnswer" -> baseURL + "flashcards";
           case "matchWordsWithTranslations" -> baseURL + "flashcards/matching";
           case "mix" -> baseURL; //will be corrected
           default -> throw new IllegalArgumentException(
                   "Illegal argument in flashCardsTrainingType " + "argument: " + flashCardsTrainingType);
       };

        }
        @GetMapping("/delete/{id}")
        public String deleteLanguageCard(Model model, @PathVariable("id") int id){
        long userId = userService.getUserId();
            if(languageCardService.findLanguageCardByIdAndUserId(id, userId) != null){
                languageCardService.deleteLanguageCardById(id, userId);
                languageCardCacheService.updateLanguageCardsForUser(userId);
            }else{
                System.out.println("This language card not exists");
            }
        return "showLanguageCardsByUser";
        }
        @GetMapping("/flashcards")
        public String showFlashCardsPage(){
        return "flashCards";
        }
    @GetMapping("/repeat/flashcards")
    public ResponseEntity<List<FlashCardTrainingContext>> repeatWithFlashCards() {
        List<LanguageCard> languageCardList = languageCardContextHolder.getLanguageCardsToLearn();
List<FlashCardTrainingContext> flashCardTrainingContextList = flashCardService.createFlashCardTrainingList(languageCardList);

        return ResponseEntity.ok(flashCardTrainingContextList);
    }
    @GetMapping("/repeat/flashcardsMatching")
    public ResponseEntity<List<FlashCard>> getFlashCardList() {
        List<LanguageCard> languageCardList = languageCardContextHolder.getLanguageCardsToLearn();
        List<FlashCard> flashCardList = flashCardService.createMatchingTrainingList(languageCardList);

        return ResponseEntity.ok(flashCardList);
    }
    @GetMapping("/flashcards/matching")
    public String showFlashCardsMatchingPage(){
        return "flashCardsMatching";
    }
    }