package com.hololitt.SpringBootProject.controllers;

import com.hololitt.SpringBootProject.DTO.CheckAnswerDTO;
import com.hololitt.SpringBootProject.DTO.TrainingContextDTO;
import com.hololitt.SpringBootProject.enums.FlashCardsTrainingVariety;
import com.hololitt.SpringBootProject.enums.LanguageCardsWritingEnabled;
import com.hololitt.SpringBootProject.models.*;
import com.hololitt.SpringBootProject.services.*;
import com.hololitt.SpringBootProject.validators.LanguageCardValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@SuppressWarnings("unused")
@RequestMapping("/Home/WordsTrainer")
@RequiredArgsConstructor
public class WordsTrainerController {
    private final UserService userService;
    private final LanguageCardService languageCardService;
    private final LanguageCardContextHolder languageCardContextHolder;
    private final LanguageCardCacheService languageCardCacheService;
    private final WordsTrainerService wordsTrainerService;
    private final WordsTrainerSettingsService wordsTrainerSettingsService;
    private final LanguageCardValidator languageCardValidator;
    private final FlashCardService flashCardService;
    private final LastLearnedLanguageCardsService lastLearnedLanguageCardsService;
    private final UserSettingsService userSettingsService;

    @GetMapping("/cancelTraining")
    public String cancelTraining() {
        languageCardContextHolder.cleanUpContext();
        languageCardCacheService.deleteCreatedLanguageCards(userService.getUserId());
        return "redirect:/Home/WordsTrainer";
    }
@GetMapping("/startTraining")
public String defineTrainingType(){
        String baseUrl = "redirect:/Home/WordsTrainer/";
    LanguageCardsWritingEnabled languageCardsWritingEnabled =
            wordsTrainerSettingsService.getDefaultLanguageCardsWritingEnabledByUser(userService.getUserId());

    return switch(languageCardsWritingEnabled){
        case YES -> "learnLanguageCards";
        case NO -> baseUrl + "prepareLanguageCards";
    };
}

@GetMapping("/learnLanguageCards")
public ResponseEntity<List<LanguageCard>> showLanguageCardsLearningPage(){
        List<LanguageCard> languageCardsToLearn = languageCardCacheService.getCreatedLanguageCards(userService.getUserId());
        return ResponseEntity.ok(languageCardsToLearn);
}

    @GetMapping("/prepareLanguageCards")
    public String prepareLanguageCard() {
        List<LanguageCard> languageCardsToLearn = languageCardCacheService.getCreatedLanguageCards(userService.getUserId());
        return repeatLanguageCardsOrReturnException(languageCardsToLearn);
    }
    @GetMapping
    public String showWordsTrainerPage() {
        return "WordsTrainer";
    }

    @GetMapping("/preStart")
    public String preStart() {
        List<LanguageCard> languageCardsToLearn = languageCardContextHolder.getLanguageCardsToLearn();
        if (languageCardsToLearn == null) {
            return "emptyListException";
        } else {
            return "redirect:/Home/WordsTrainer/start";
        }
    }

    @GetMapping("/start")
    public String start(Model model) {
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
    public String checkAnswer(@ModelAttribute("answer") TranslationModel translationModel, Model model) {
        CheckAnswerDTO result = wordsTrainerService.checkAnswer(translationModel);

        FlashCardsTrainingVariety flashTrainingType = wordsTrainerSettingsService.getSettingsForUser(userService.getUserId())
                .getFlashCardsTrainingVariety();
        if (result.isRepeatFlashCardsTraining()) {
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
    public String finish(Model model) {
        List<LanguageCard> languageCardList = languageCardContextHolder.getLearnedLanguageCards();
        Map<LanguageCard, Integer> mistakesDuringTraining = languageCardContextHolder.getAllMistakesDuringTraining();

        model.addAttribute("learnedLanguageCards", languageCardList);
        model.addAttribute("mistakesDuringTraining", mistakesDuringTraining);

        List<LastLearnedLanguageCard> lastLearnedLanguageCards = lastLearnedLanguageCardsService.
                convertToLastLearnedLanguageCards(languageCardList);

        lastLearnedLanguageCardsService.updateLastLearnedLanguageCards(lastLearnedLanguageCards, (int) userService.getUserId());
        lastLearnedLanguageCardsService.deleteCache((int) userService.getUserId());

        Set<LanguageCard> languageCards = new HashSet<>(languageCardList);
        languageCardService.saveLanguageCardList(languageCards);

        languageCardCacheService.updateLanguageCardsForUser(userService.getUserId());
        languageCardCacheService.deleteCreatedLanguageCards(userService.getUserId());
        return "finish";
    }

    @GetMapping("/repeat")
    public String repeatLanguageCards() {
        List<LanguageCard> languageCards = languageCardService.getLanguageCardsByUserId(userService.getUserId());
        return repeatLanguageCardsOrReturnException(languageCards);
    }

    @GetMapping("/repeatMostDifficult")
    public String repeatMostDifficult() {
        List<LanguageCard> languageCards = wordsTrainerService.getMostDifficultLanguageCardsToLearn();
        return repeatLanguageCardsOrReturnException(languageCards);
    }

    @GetMapping("/repeatRecommended")
    public String startRecommendedTraining() {
        List<LanguageCard> languageCards = wordsTrainerService.getRecommendedLanguageCardsToLearn();
        return repeatLanguageCardsOrReturnException(languageCards);
    }

    @GetMapping("/repeatRandom")
    public String repeatRandomLanguageCards() {
        List<LanguageCard> languageCards = wordsTrainerService.getRandomLanguageCardsToLearn();
        return repeatLanguageCardsOrReturnException(languageCards);
    }

    @GetMapping("/repeatLastLearned")
    public String repeatLastLearned() {
        int userId = (int) userService.getUserId();

        List<LastLearnedLanguageCard> lastLearnedLanguageCards = lastLearnedLanguageCardsService.
                findLastLearnedLanguageCards(userId);

        List<LanguageCard> languageCards = lastLearnedLanguageCardsService.convertToLanguageCards(lastLearnedLanguageCards);
        return repeatLanguageCardsOrReturnException(languageCards);
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

    private String defineFlashCardsTrainingType(FlashCardsTrainingVariety flashCardsTrainingVariety) {
        String baseURL = "redirect:/Home/WordsTrainer/";

        return switch (flashCardsTrainingVariety) {
            case NO_TRAINING -> baseURL + "start";
            case CHOOSE_ANSWER -> baseURL + "flashcards";
            case MATCH_WORDS_WITH_TRANSLATIONS -> baseURL + "flashcards/matching";
            case MIX -> baseURL; //will be corrected
        };
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
}