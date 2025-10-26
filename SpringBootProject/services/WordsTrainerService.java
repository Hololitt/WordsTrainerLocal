package com.hololitt.SpringBootProject.services;

import com.hololitt.SpringBootProject.DTO.CheckAnswerDTO;
import com.hololitt.SpringBootProject.enums.TrainingType;
import com.hololitt.SpringBootProject.models.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WordsTrainerService {
    private final LanguageCardCacheService languageCardCacheService;
    private final LanguageCardContextHolder languageCardContextHolder;
    private final UserService userService;
    private final UserSettingsService userSettingsService;


    private UserSettings getUserSettings() {
        return userSettingsService.getUserSettings(userService.getUserId());
    }

    public void setTrainingType() {
        TrainingType trainingType = getUserSettings().getTranslationRequestVariety();
        switch (trainingType) {
            case MIX -> languageCardContextHolder.generateRandomValueAndLanguageCard();
            case WORD_TO_TRANSLATION -> languageCardContextHolder.generateRandomLanguageCardAndTranslation();
            case TRANSLATION_TO_WORD -> languageCardContextHolder.generateRandomLanguageCardAndWord();
            default -> throw new IllegalArgumentException("Unknown training type: " + trainingType);
        }
    }
    public CheckAnswerDTO checkAnswer(TranslationModel translationModel) {
        String answer = translationModel.getAnswer();
        LanguageCard randomLanguageCard = languageCardContextHolder.getRandomLanguageCard();
        String randomValue = languageCardContextHolder.getRandomValue();
        String correctAnswer = languageCardContextHolder.defineCorrectAnswer(randomValue, randomLanguageCard);

        if (!isAnswerCorrect(randomLanguageCard, answer, correctAnswer)) {
            return handleIncorrectAnswer(randomLanguageCard, correctAnswer);
        }

        return handleCorrectAnswer(randomLanguageCard, correctAnswer);
    }

    private boolean isAnswerCorrect(LanguageCard randomLanguageCard, String answer, String correctAnswer) {
        return languageCardContextHolder.isCorrectAnswer(randomLanguageCard, answer, correctAnswer);
    }

    private CheckAnswerDTO handleIncorrectAnswer(LanguageCard randomLanguageCard, String correctAnswer) {
        randomLanguageCard.incrementMistakesCount();

        if(getUserSettings().getCorrectAnswersDecrementIfMistakeEnabled()){
            languageCardContextHolder.decrementCorrectAnswersCount(randomLanguageCard);
        }
        languageCardContextHolder.incrementMistakesCountDuringTraining(randomLanguageCard);
        languageCardContextHolder.incrementMistakesCount();

        if((languageCardContextHolder.getMistakesCount() % 3) == 0){
            return new CheckAnswerDTO(false, true, correctAnswer);
        }

        return new CheckAnswerDTO(false, false, correctAnswer);
    }

    private CheckAnswerDTO handleCorrectAnswer(LanguageCard randomLanguageCard, String correctAnswer) {
        int correctAnswersCount = languageCardContextHolder.getCorrectAnswers(randomLanguageCard);
        int correctAnswersCountToFinish = getUserSettings().getCorrectAnswersCountToFinish();

        if (correctAnswersCount == correctAnswersCountToFinish) {
            randomLanguageCard.incrementRepeatCount();
            randomLanguageCard.setLastRepetition(LocalDateTime.now(ZoneId.of("Europe/Berlin")));
            randomLanguageCard.setIsLastLearned(true);

            languageCardContextHolder.addLearnedLanguageCard(randomLanguageCard);
            languageCardContextHolder.removeFromCorrectAnswers(randomLanguageCard);
            languageCardContextHolder.removeFromLanguageCardsToLearn(randomLanguageCard);
        }

        if (languageCardContextHolder.getLanguageCardsToLearn().isEmpty()) {
            return new CheckAnswerDTO(true, false, null);
        }

        return new CheckAnswerDTO(true, false, correctAnswer);
    }

    public List<LanguageCard> getMostDifficultLanguageCardsToLearn() {
        List<LanguageCard> languageCards = languageCardCacheService.getLanguageCardsByUser();
        int countLanguageCardsToRepeat = getUserSettings().getCountLanguageCardsToRepeat();
        languageCards.sort(Comparator.comparingInt(LanguageCard::getMistakesCount).reversed());
        return languageCards.stream().limit(countLanguageCardsToRepeat).collect(Collectors.toList());
    }

    public List<LanguageCard> getRecommendedLanguageCardsToLearn() {
        List<LanguageCard> languageCards = languageCardCacheService.getLanguageCardsByUser();
        int countLanguageCardsToRepeat = getUserSettings().getCountLanguageCardsToRepeat();
        return languageCards.stream()
                .sorted(Comparator.comparingInt(LanguageCard::getRepeatCount)).limit(countLanguageCardsToRepeat)
                .collect(Collectors.toList());
    }
public List<LanguageCard> getRandomLanguageCardsToLearn(){
        List<LanguageCard> languageCards = languageCardCacheService.getLanguageCardsByUser();
        Collections.shuffle(languageCards, new Random());
    int countLanguageCardsToRepeat = getUserSettings().getCountLanguageCardsToRepeat();
    return languageCards.stream().limit(countLanguageCardsToRepeat).collect(Collectors.toList());
}
    public int calculateCorrectAnswersCount(LanguageCard randomLanguageCard) {
        return languageCardContextHolder.getCorrectAnswers().isEmpty() ? 0 :
                languageCardContextHolder.getCorrectAnswers(randomLanguageCard);
    }
    public int calculateProgressPercent(List<LanguageCard> languageCardsToLearn){
        int languageCardsCount = languageCardsToLearn.size();
        int answersToLearnLanguageCard = getUserSettings().getCorrectAnswersCountToFinish();
        int correctAnswersAmountToFinish = languageCardsCount * answersToLearnLanguageCard;
        int currentCorrectAnswersAmount = 0;

        for(LanguageCard languageCard : languageCardsToLearn){
            currentCorrectAnswersAmount += languageCardContextHolder.getCorrectAnswers(languageCard);
        }

        if(currentCorrectAnswersAmount == 0){
            return 0;
        }

        float res1 = (float) currentCorrectAnswersAmount / correctAnswersAmountToFinish;

        return (int) (res1 * 100);
    }
}
