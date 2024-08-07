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
    public void setAllSettings(WordsTrainerSettings wordsTrainerSettings){
       int countCorrectAnswersToFinish = wordsTrainerSettings.getCorrectAnswersCountToFinish();
       int countLanguageCardsToRepeat = wordsTrainerSettings.getCountLanguageCardsToRepeat();
        String translationRequestVariety = wordsTrainerSettings.getTranslationRequestVariety();
        long userId = wordsTrainerSettings.getUserId();
       if(countCorrectAnswersToFinish <= 0){
          countCorrectAnswersToFinish = defaultCountCorrectAnswersToFinish;
       }
       if(countLanguageCardsToRepeat <= 0){
           countLanguageCardsToRepeat = defaultCountLanguageCardsToRepeat;
       }
       if(!allowedValues.contains(translationRequestVariety)){
           translationRequestVariety = defaultTranslationRequestVariety;
       }
       WordsTrainerSettings wordsTrainerSettingsForMap = new WordsTrainerSettings(countCorrectAnswersToFinish,
               translationRequestVariety, countLanguageCardsToRepeat, userId);
userSettingsMap.put(userId, wordsTrainerSettingsForMap);
    }
    public WordsTrainerSettings getSettingsForUser(long userId) {
        WordsTrainerSettings settings = userSettingsMap.get(userId);
        if(settings == null){
            userSettingsMap.put(userId, new WordsTrainerSettings(defaultCountCorrectAnswersToFinish,
                    defaultTranslationRequestVariety, defaultCountLanguageCardsToRepeat, userId));
        }
        return userSettingsMap.get(userId);
    }

}
