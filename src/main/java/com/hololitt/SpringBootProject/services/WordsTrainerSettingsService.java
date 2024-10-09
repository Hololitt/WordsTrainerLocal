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

        countCorrectAnswersToFinish = (countCorrectAnswersToFinish > 0) ? countCorrectAnswersToFinish : defaultCountCorrectAnswersToFinish;
        countLanguageCardsToRepeat = (countLanguageCardsToRepeat > 0) ? countLanguageCardsToRepeat : defaultCountLanguageCardsToRepeat;
        translationRequestVariety = allowedValues.contains(translationRequestVariety) ? translationRequestVariety : defaultTranslationRequestVariety;

        return new WordsTrainerSettings(countCorrectAnswersToFinish,
                translationRequestVariety, countLanguageCardsToRepeat, settings.getUserId());
    }

    private WordsTrainerSettings createDefaultSettings(long userId) {
        return new WordsTrainerSettings(defaultCountCorrectAnswersToFinish,
                defaultTranslationRequestVariety, defaultCountLanguageCardsToRepeat, userId);
    }
}