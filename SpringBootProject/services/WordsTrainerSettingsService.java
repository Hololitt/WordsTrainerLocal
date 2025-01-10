package com.hololitt.SpringBootProject.services;

import com.hololitt.SpringBootProject.enums.FlashCardsTrainingVariety;
import com.hololitt.SpringBootProject.enums.LanguageCardsWritingEnabled;
import com.hololitt.SpringBootProject.enums.TrainingType;
import com.hololitt.SpringBootProject.models.DefaultWordsTrainerSettings;
import com.hololitt.SpringBootProject.models.WordsTrainerSettings;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WordsTrainerSettingsService {
    private final Map<Long, WordsTrainerSettings> userSettingsMap = new ConcurrentHashMap<>();
    private final int defaultCountCorrectAnswersToFinish = DefaultWordsTrainerSettings.DEFAULT_COUNT_CORRECT_ANSWERS_TO_FINISH;
    private final int defaultCountLanguageCardsToRepeat = DefaultWordsTrainerSettings.DEFAULT_COUNT_LANGUAGE_CARDS_TO_REPEAT;
    private final TrainingType defaultTranslationRequestVariety = DefaultWordsTrainerSettings.DEFAULT_TRANSLATION_REQUEST_VARIETY;
    private final FlashCardsTrainingVariety defaultFlashCardsTrainingVariety =
            DefaultWordsTrainerSettings.DEFAULT_FLASH_CARDS_TRAINING_VARIETY;
    private final LanguageCardsWritingEnabled defaultLanguageCardsWritingEnabled =
            DefaultWordsTrainerSettings.DEFAULT_LANGUAGE_CARD_WRITING_ENABLED;

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

        TrainingType translationRequestVariety = settings.getTranslationRequestVariety();
FlashCardsTrainingVariety flashCardsTrainingVariety = settings.getFlashCardsTrainingVariety();

LanguageCardsWritingEnabled languageCardsWritingEnabled = settings.getLanguageCardsWritingEnabled();

        countCorrectAnswersToFinish = validateCountCorrectAnswersToFinish(countCorrectAnswersToFinish);

        countLanguageCardsToRepeat = validateCountLanguageCardsToRepeat(countLanguageCardsToRepeat);

        translationRequestVariety = validateTranslationRequestVariety(translationRequestVariety);

        flashCardsTrainingVariety = validateFlashCardsTrainingVariety(flashCardsTrainingVariety);

        languageCardsWritingEnabled = validateLanguageCardsWritingEnabled(languageCardsWritingEnabled);

        return new WordsTrainerSettings(countCorrectAnswersToFinish,
                translationRequestVariety, countLanguageCardsToRepeat, settings.getUserId(), flashCardsTrainingVariety,
                languageCardsWritingEnabled);
    }

    private int validateCountCorrectAnswersToFinish(int countCorrectAnswersToFinish){
        return (countCorrectAnswersToFinish > 0) ? countCorrectAnswersToFinish : defaultCountCorrectAnswersToFinish;
    }

    private int validateCountLanguageCardsToRepeat(int countLanguageCardsToRepeat){
        return (countLanguageCardsToRepeat > 0) ? countLanguageCardsToRepeat : defaultCountLanguageCardsToRepeat;
    }

    private TrainingType validateTranslationRequestVariety(TrainingType trainingType){
        return (trainingType != null) ? trainingType : defaultTranslationRequestVariety;
    }
    private FlashCardsTrainingVariety validateFlashCardsTrainingVariety(FlashCardsTrainingVariety flashCardsTrainingVariety){
        return (flashCardsTrainingVariety != null) ? flashCardsTrainingVariety : defaultFlashCardsTrainingVariety;
    }
    private LanguageCardsWritingEnabled validateLanguageCardsWritingEnabled(LanguageCardsWritingEnabled
                                                                                    languageCardsWritingEnabled){
        return (languageCardsWritingEnabled != null) ? languageCardsWritingEnabled : defaultLanguageCardsWritingEnabled;
    }
    private WordsTrainerSettings createDefaultSettings(long userId) {
        return new WordsTrainerSettings(defaultCountCorrectAnswersToFinish,
                defaultTranslationRequestVariety, defaultCountLanguageCardsToRepeat, userId, defaultFlashCardsTrainingVariety,
               defaultLanguageCardsWritingEnabled);
    }
    public LanguageCardsWritingEnabled getDefaultLanguageCardsWritingEnabledByUser(long userId){
        return userSettingsMap.computeIfAbsent(userId, this::createDefaultSettings).getLanguageCardsWritingEnabled();
    }
}