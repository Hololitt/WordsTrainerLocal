package com.hololitt.SpringBootProject.services;

import com.hololitt.SpringBootProject.DTO.CheckAnswerDTO;
import com.hololitt.SpringBootProject.models.LanguageCard;
import com.hololitt.SpringBootProject.models.LanguageCardContextHolder;
import com.hololitt.SpringBootProject.models.LanguageCardCreationForm;
import com.hololitt.SpringBootProject.models.TranslationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WordsTrainerService {
    private final LanguageCardCacheService languageCardCacheService;
    private final LanguageCardContextHolder languageCardContextHolder = new LanguageCardContextHolder();
    @Autowired
    public WordsTrainerService(LanguageCardCacheService languageCardCacheService) {
        this.languageCardCacheService = languageCardCacheService;
    }
    public LanguageCardContextHolder getLanguageCardContextHolder(){
        return languageCardContextHolder;
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
        return new CheckAnswerDTO(false, correctAnswer);
    }
    int correctAnswersCount = languageCardContextHolder.getCorrectAnswers(randomLanguageCard);
    int correctAnswersCountToFinish = languageCardContextHolder.getCountCorrectAnswersToFinish();

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
    public void setLanguageCardsToLearn(List<LanguageCard> languageCardsToLearn){
            languageCardContextHolder.cleanUpContext();
        languageCardContextHolder.setLanguageCardsToLearn(languageCardsToLearn);
    }
    public boolean isListValid(List<?> list){
        if (list == null || list.isEmpty()) {
            return false;
        }else{
            return true;
        }
    }
    public int calculateCorrectAnswersCount(LanguageCard randomLanguageCard) {
        if (languageCardContextHolder.getCorrectAnswers().isEmpty()) {
            return 0;
        } else {
            return languageCardContextHolder.getCorrectAnswers(randomLanguageCard);
        }
    }
}