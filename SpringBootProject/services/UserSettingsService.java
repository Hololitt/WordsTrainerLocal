package com.hololitt.SpringBootProject.services;

import com.hololitt.SpringBootProject.models.DefaultWordsTrainerSettings;
import com.hololitt.SpringBootProject.models.UserSettings;
import com.hololitt.SpringBootProject.repositorys.UserSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSettingsService {
    private UserSettingsRepository userSettingsRepository;

    @CachePut(value = "userSettings", key = "#userId")
    public void updateUserSettings(UserSettings userSettings, long userId){
        userSettingsRepository.deleteAllByUserId(userId);
        userSettingsRepository.save(userSettings);
    }

    @Cacheable(value = "userSettings", key = "#userId")
    public UserSettings getUserSettings(long userId){
      UserSettings userSettings = userSettingsRepository.findByUserId(userId);

      if(userSettings == null){
          setDefaultUserSettings(userId);

          userSettings = userSettingsRepository.findByUserId(userId);
      }
      return userSettings;
    }
    @CacheEvict(value = "userSettings", key = "#userId")
    public void deleteCachedUserSettings(long userId){}

    @CachePut(value = "userSettings", key = "#userId")
    public void setDefaultUserSettings(long userId){
userSettingsRepository.save(UserSettings.builder()
        .userId(userId)
        .correctAnswersCountToFinish(DefaultWordsTrainerSettings.DEFAULT_COUNT_CORRECT_ANSWERS_TO_FINISH)
        .countLanguageCardsToRepeat(DefaultWordsTrainerSettings.DEFAULT_COUNT_LANGUAGE_CARDS_TO_REPEAT)
        .flashCardsTrainingVariety(DefaultWordsTrainerSettings.DEFAULT_FLASH_CARDS_TRAINING_VARIETY)
        .languageCardsWritingEnabled(DefaultWordsTrainerSettings.DEFAULT_LANGUAGE_CARD_WRITING_ENABLED)
        .translationRequestVariety(DefaultWordsTrainerSettings.DEFAULT_TRANSLATION_REQUEST_VARIETY)
        .build());
    }

}
