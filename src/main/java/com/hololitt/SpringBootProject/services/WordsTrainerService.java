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

    private WordsTrainerSettings getWordsTrainerSettingsForUser() {
        return wordsTrainerSettingsService.getSettingsForUser(userService.getUserId());
    }

    public void setTrainingType() {
        TrainingType trainingType = getTrainingType();
        switch (trainingType) {
            case MIX -> languageCardContextHolder.generateRandomValueAndLanguageCard();
            case WORD_TO_TRANSLATION -> languageCardContextHolder.generateRandomLanguageCardAndTranslation();
            case TRANSLATION_TO_WORD -> languageCardContextHolder.generateRandomLanguageCardAndWord();
            default -> throw new IllegalArgumentException("Unknown training type: " + trainingType);
        }
    }
private TrainingType getTrainingType(){
    String type = getWordsTrainerSettingsForUser().getTranslationRequestVariety();
    return TrainingType.chooseType(type);
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
        languageCardContextHolder.decrementCorrectAnswersCount(randomLanguageCard);
        languageCardContextHolder.incrementMistakesCountDuringTraining(randomLanguageCard);
        return new CheckAnswerDTO(false, correctAnswer);
    }

    private CheckAnswerDTO handleCorrectAnswer(LanguageCard randomLanguageCard, String correctAnswer) {
        int correctAnswersCount = languageCardContextHolder.getCorrectAnswers(randomLanguageCard);
        int correctAnswersCountToFinish = getWordsTrainerSettingsForUser().getCorrectAnswersCountToFinish();

        if (correctAnswersCount == correctAnswersCountToFinish) {
            randomLanguageCard.incrementRepeatCount();
            languageCardContextHolder.addLearnedLanguageCard(randomLanguageCard);
            languageCardContextHolder.removeFromCorrectAnswers(randomLanguageCard);
            languageCardContextHolder.removeFromLanguageCardsToLearn(randomLanguageCard);
        }

        if (languageCardContextHolder.getLanguageCardsToLearn().isEmpty()) {
            return new CheckAnswerDTO(true, null);
        }

        return new CheckAnswerDTO(true, correctAnswer);
    }

    public List<LanguageCard> getMostDifficultLanguageCardsToLearn() {
        List<LanguageCard> languageCards = languageCardCacheService.getLanguageCardsByUser();
        int countLanguageCardsToRepeat = getWordsTrainerSettingsForUser().getCountLanguageCardsToRepeat();
        languageCards.sort(Comparator.comparingInt(LanguageCard::getMistakesCount).reversed());
        return languageCards.stream().limit(countLanguageCardsToRepeat).collect(Collectors.toList());
    }

    public List<LanguageCard> getRecommendedLanguageCardsToLearn() {
        List<LanguageCard> languageCards = languageCardCacheService.getLanguageCardsByUser();
        int countLanguageCardsToRepeat = getWordsTrainerSettingsForUser().getCountLanguageCardsToRepeat();
        return languageCards.stream()
                .sorted(Comparator.comparingInt(LanguageCard::getRepeatCount)).limit(countLanguageCardsToRepeat)
                .collect(Collectors.toList());
    }
public List<LanguageCard> getRandomLanguageCardsToLearn(){
        List<LanguageCard> languageCards = languageCardCacheService.getLanguageCardsByUser();
    int countLanguageCardsToRepeat = getWordsTrainerSettingsForUser().getCountLanguageCardsToRepeat();
    return languageCards.stream().limit(countLanguageCardsToRepeat).collect(Collectors.toList());
}
    public int calculateCorrectAnswersCount(LanguageCard randomLanguageCard) {
        return languageCardContextHolder.getCorrectAnswers().isEmpty() ? 0 :
                languageCardContextHolder.getCorrectAnswers(randomLanguageCard);
    }
}
 enum TrainingType{
    MIX("mix"),
     WORD_TO_TRANSLATION("word to translation"),
     TRANSLATION_TO_WORD("translation to word");
private final String value;
     TrainingType(String value){
         this.value = value;
     }
     public static TrainingType chooseType(String type){
         for(TrainingType trainingType : TrainingType.values()){
             if(trainingType.value.equals(type)){
return trainingType;
             }
         }
         throw new IllegalArgumentException();
     }
}