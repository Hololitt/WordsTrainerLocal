package com.hololitt.SpringBootProject.services;

import com.hololitt.SpringBootProject.DTO.CheckAnswerDTO;
import com.hololitt.SpringBootProject.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WordsTrainerService {
    private final LanguageCardCacheService languageCardCacheService;
    private final LanguageCardContextHolder languageCardContextHolder;
    private final UserService userService;
    private final WordsTrainerSettingsService wordsTrainerSettingsService;
    @Autowired
    public WordsTrainerService(LanguageCardCacheService languageCardCacheService,
                               LanguageCardContextHolder languageCardContextHolder,
                               UserService userService,
                               WordsTrainerSettingsService wordsTrainerSettingsService) {
        this.languageCardCacheService = languageCardCacheService;
        this.languageCardContextHolder = languageCardContextHolder;
        this.userService = userService;
        this.wordsTrainerSettingsService = wordsTrainerSettingsService;
    }
    private WordsTrainerSettings getWordsTrainerSettingsForUser(){
        return wordsTrainerSettingsService.getSettingsForUser(userService.getUserId());
    }
    public void setTrainingType(){
        String trainingType = getWordsTrainerSettingsForUser().getTranslationRequestVariety();
        switch(trainingType){
            case "mix" -> languageCardContextHolder.generateRandomValueAndLanguageCard();
            case "word to translation"-> languageCardContextHolder.generateRandomLanguageCardAndTranslation();
            case "translation to word"-> languageCardContextHolder.generateRandomLanguageCardAndWord();
            default -> throw new IllegalArgumentException("Unknown training type: " + trainingType);
        }
    }
public void createLanguageCard(LanguageCardCreationForm languageCardCreationForm, long userId){
        String word = languageCardCreationForm.getWord();
        String translation = languageCardCreationForm.getTranslation();
        languageCardContextHolder.addLanguageCardToLearn(new LanguageCard(word, translation, userId));
}
public CheckAnswerDTO checkAnswer(TranslationModel translationModel){
    String answer = translationModel.getAnswer();
    LanguageCard randomLanguageCard = languageCardContextHolder.getRandomLanguageCard();
    String randomValue = languageCardContextHolder.getRandomValue();
    String correctAnswer = languageCardContextHolder.getCorrectAnswer(randomValue, randomLanguageCard);
    if(!languageCardContextHolder.isCorrectAnswer(randomLanguageCard, answer, correctAnswer)){
        randomLanguageCard.incrementMistakesCount();
        languageCardContextHolder.decrementCorrectAnswersCount(randomLanguageCard);
        languageCardContextHolder.incrementMistakesCountDuringTraining(randomLanguageCard);
        return new CheckAnswerDTO(false, correctAnswer);
    }
    int correctAnswersCount = languageCardContextHolder.getCorrectAnswers(randomLanguageCard);
    int correctAnswersCountToFinish = getWordsTrainerSettingsForUser().getCorrectAnswersCountToFinish();

    if(correctAnswersCount == correctAnswersCountToFinish){
        randomLanguageCard.incrementRepeatCount();
        languageCardContextHolder.addLearnedLanguageCard(randomLanguageCard);
        languageCardContextHolder.removeFromCorrectAnswers(randomLanguageCard);
        languageCardContextHolder.removeFromLanguageCardsToLearn(randomLanguageCard);
    }
    if(languageCardContextHolder.getLanguageCardsToLearn().isEmpty()){
        return new CheckAnswerDTO(true, null);
    }
    return new CheckAnswerDTO(true, correctAnswer);
}
public List<LanguageCard> getMostDifficultLanguageCardsToLearn(){
    List<LanguageCard> languageCards = languageCardCacheService.getLanguageCardsByUser();
    languageCards.sort(Comparator.comparingInt(LanguageCard::getMistakesCount).reversed());
    languageCards = languageCards.stream().limit(5).collect(Collectors.toList());
    return languageCards;
}
public List<LanguageCard> getRecommendedLanguageCardsToLearn(){
    List<LanguageCard> languageCards = languageCardCacheService.getLanguageCardsByUser();
    languageCards = languageCards.stream()
            .sorted(Comparator.comparingInt(LanguageCard::getRepeatCount)).limit(5).collect(Collectors.toList());
    return languageCards;
}
    public int calculateCorrectAnswersCount(LanguageCard randomLanguageCard) {
        if (languageCardContextHolder.getCorrectAnswers().isEmpty()) {
            return 0;
        } else {
            return languageCardContextHolder.getCorrectAnswers(randomLanguageCard);
        }
    }
}