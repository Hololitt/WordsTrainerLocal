package com.hololitt.SpringBootProject.services;

import com.hololitt.SpringBootProject.models.WordsTrainerSettings;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WordsTrainerSettingsService {
    private final Map<Long, WordsTrainerSettings> userSettingsMap = new HashMap<>();
    private final int defaultCountCorrectAnswersToFinish = 3;
    private final int defaultCountLanguageCardsToRepeat = 5;
    private final String defaultTranslationRequestVariety = "mix";
    private final String defaultFlashCardsTrainingVariety = "chooseAnswer";
    private final List<String> allowedValues = Arrays.asList("mix", "translation to word", "word to translation");

    public void setAllSettings(WordsTrainerSettings wordsTrainerSettings) {
        long userId = wordsTrainerSettings.getUserId();
        WordsTrainerSettings validatedSettings = validateSettings(wordsTrainerSettings);
        userSettingsMap.put(userId, validatedSettings);
    }

    public WordsTrainerSettings getSettingsForUser(long userId) {
        return userSettingsMap.computeIfAbsent(userId, this::createDefaultSettings);
    }

    private WordsTrainerSettings validateSettings(WordsTrainerSettings settings) {
        int countCorrectAnswersToFinish = settings.getCorrectAnswersCountToFinish();
        int countLanguageCardsToRepeat = settings.getCountLanguageCardsToRepeat();
        String translationRequestVariety = settings.getTranslationRequestVariety();
String flashCardsTrainingVariety = settings.getFlashCardsTrainingVariety();

        countCorrectAnswersToFinish = (countCorrectAnswersToFinish > 0) ? countCorrectAnswersToFinish : defaultCountCorrectAnswersToFinish;
        countLanguageCardsToRepeat = (countLanguageCardsToRepeat > 0) ? countLanguageCardsToRepeat : defaultCountLanguageCardsToRepeat;
        translationRequestVariety = allowedValues.contains(translationRequestVariety) ? translationRequestVariety : defaultTranslationRequestVariety;
flashCardsTrainingVariety = FlashCardsTrainingVariety.chooseType(flashCardsTrainingVariety);

        return new WordsTrainerSettings(countCorrectAnswersToFinish,
                translationRequestVariety, countLanguageCardsToRepeat, settings.getUserId(), flashCardsTrainingVariety);
    }

    private WordsTrainerSettings createDefaultSettings(long userId) {
        return new WordsTrainerSettings(defaultCountCorrectAnswersToFinish,
                defaultTranslationRequestVariety, defaultCountLanguageCardsToRepeat, userId, defaultFlashCardsTrainingVariety);
    }
}
enum FlashCardsTrainingVariety{
    MIX("mix"),
    CHOOSE_ANSWER("chooseAnswer"),
    MATCH_WORDS_WITH_TRANSLATIONS("matchWordsWithTranslations"),
    NO_TRAINING("noTraining");
    private final String type;
    FlashCardsTrainingVariety(String type){
        this.type = type;
    }
    public static String chooseType(String type){
        for(FlashCardsTrainingVariety flashCardsTrainingVariety : FlashCardsTrainingVariety.values()){
            if(flashCardsTrainingVariety.type.equals(type)){
                return flashCardsTrainingVariety.type;
            }
        }
        throw new IllegalArgumentException("Illegal FlashCardsTrainingVariety type");
    }
}