package com.hololitt.SpringBootProject.services;

import com.hololitt.SpringBootProject.models.WordsTrainerSettings;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class WordsTrainerSettingsService {
    private final Map<Long, WordsTrainerSettings> userSettingsMap = new HashMap<>();
    private final int defaultCountCorrectAnswersToFinish = 3;
    private final String defaultTranslationRequestVariety = "mix";
    public void setAllSettings(WordsTrainerSettings wordsTrainerSettings){
       int countCorrectAnswersToFinish = wordsTrainerSettings.getCorrectAnswersCountToFinish();
        String translationRequestVariety = wordsTrainerSettings.getTranslationRequestVariety();
        long userId = wordsTrainerSettings.getUserId();
       if(countCorrectAnswersToFinish <= 0){
          countCorrectAnswersToFinish = defaultCountCorrectAnswersToFinish;
       }
       if(Objects.equals(translationRequestVariety, "")){
           translationRequestVariety = defaultTranslationRequestVariety;
       }
       WordsTrainerSettings wordsTrainerSettingsForMap = new WordsTrainerSettings(countCorrectAnswersToFinish,
               translationRequestVariety, userId);
userSettingsMap.put(userId, wordsTrainerSettingsForMap);
    }
    public WordsTrainerSettings getSettingsForUser(long userId) {
        WordsTrainerSettings settings = userSettingsMap.get(userId);
        if(settings == null){
            userSettingsMap.put(userId, new WordsTrainerSettings(defaultCountCorrectAnswersToFinish,
                    defaultTranslationRequestVariety, userId));
        }
        return userSettingsMap.get(userId);
    }

}
