package com.hololitt.SpringBootProject.services;

import com.hololitt.SpringBootProject.enums.FlashCardsTrainingVariety;
import com.hololitt.SpringBootProject.enums.LanguageCardsWritingEnabled;
import com.hololitt.SpringBootProject.enums.TrainingType;
import com.hololitt.SpringBootProject.models.DefaultWordsTrainerSettings;
import com.hololitt.SpringBootProject.models.UserSettings;
import com.hololitt.SpringBootProject.repositorys.UserSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserSettingsService {
    private final UserSettingsRepository userSettingsRepository;

    @CacheEvict(value = "userSettings", key = "#userId")
    @CachePut(value = "userSettings", key = "#userId")
    @Transactional
    public void updateUserSettings(UserSettings newUserSettings, long userId){
        UserSettings currentUserSettings = userSettingsRepository.findByUserId(userId);

         if(currentUserSettings != null){
             validateSettings(newUserSettings, currentUserSettings);
             userSettingsRepository.save(currentUserSettings);
         }else{
newUserSettings.setUserId(userId);
userSettingsRepository.save(newUserSettings);
        }
    }

    private void validateSettings(UserSettings newUserSettings, UserSettings currentUserSettings){
        int countCorrectAnswersToFinish =
                validateCountCorrectAnswersToFinish(newUserSettings.getCorrectAnswersCountToFinish(),
                        currentUserSettings.getCorrectAnswersCountToFinish());

        FlashCardsTrainingVariety flashCardsTrainingVariety =
                validateFlashCardsTrainingVariety(newUserSettings.getFlashCardsTrainingVariety(),
                        currentUserSettings.getFlashCardsTrainingVariety());

        int countLanguageCardsToRepeat = validateCountLanguageCardsToRepeat(newUserSettings.getCountLanguageCardsToRepeat(),
                currentUserSettings.getCountLanguageCardsToRepeat());

        LanguageCardsWritingEnabled languageCardsWritingEnabled =
                validateLanguageCardsWritingEnabled(newUserSettings.getLanguageCardsWritingEnabled(),
                        currentUserSettings.getLanguageCardsWritingEnabled());

        TrainingType trainingType = validateTranslationRequestVariety(newUserSettings.getTranslationRequestVariety(),
                currentUserSettings.getTranslationRequestVariety());

        Boolean correctAnswersDecrementEnabled = validateCorrectAnswersDecrementIfMistakeEnabled(
                newUserSettings.getCorrectAnswersDecrementIfMistakeEnabled(),
                currentUserSettings.getCorrectAnswersDecrementIfMistakeEnabled());


        currentUserSettings.setCorrectAnswersCountToFinish(countCorrectAnswersToFinish);
        currentUserSettings.setFlashCardsTrainingVariety(flashCardsTrainingVariety);
        currentUserSettings.setCountLanguageCardsToRepeat(countLanguageCardsToRepeat);
        currentUserSettings.setLanguageCardsWritingEnabled(languageCardsWritingEnabled);
        currentUserSettings.setTranslationRequestVariety(trainingType);
        currentUserSettings.setCorrectAnswersDecrementIfMistakeEnabled(correctAnswersDecrementEnabled);
    }

    @Cacheable(value = "userSettings", key = "#userId")
    public UserSettings getUserSettings(long userId) {
        UserSettings userSettings = userSettingsRepository.findByUserId(userId);

        if (userSettings == null) {
            setDefaultUserSettings(userId);
            userSettings = userSettingsRepository.findByUserId(userId);
        }

        return userSettings;
    }

    @CachePut(value = "userSettings", key = "#userId")
    @Transactional
    public void setDefaultUserSettings(long userId){
userSettingsRepository.save(UserSettings.builder()
        .userId(userId)
        .correctAnswersCountToFinish(DefaultWordsTrainerSettings.DEFAULT_COUNT_CORRECT_ANSWERS_TO_FINISH)
        .countLanguageCardsToRepeat(DefaultWordsTrainerSettings.DEFAULT_COUNT_LANGUAGE_CARDS_TO_REPEAT)
        .flashCardsTrainingVariety(DefaultWordsTrainerSettings.DEFAULT_FLASH_CARDS_TRAINING_VARIETY)
        .languageCardsWritingEnabled(DefaultWordsTrainerSettings.DEFAULT_LANGUAGE_CARD_WRITING_ENABLED)
        .translationRequestVariety(DefaultWordsTrainerSettings.DEFAULT_TRANSLATION_REQUEST_VARIETY)
                .correctAnswersDecrementIfMistakeEnabled
                        (DefaultWordsTrainerSettings.DEFAULT_CORRECT_ANSWERS_DECREMENT_IF_MISTAKE_ENABLED)
        .build());
    }

    private int validateCountCorrectAnswersToFinish(int countCorrectAnswersToFinish, int currentValue){
        if(countCorrectAnswersToFinish > 0){
            return countCorrectAnswersToFinish;
        }

        return (currentValue > 0) ? currentValue :
                DefaultWordsTrainerSettings.DEFAULT_COUNT_CORRECT_ANSWERS_TO_FINISH;
    }

    private int validateCountLanguageCardsToRepeat(int countLanguageCardsToRepeat, int currentValue){
        if(countLanguageCardsToRepeat > 0){
            return countLanguageCardsToRepeat;
        }
        return (currentValue > 0) ? currentValue :
                DefaultWordsTrainerSettings.DEFAULT_COUNT_LANGUAGE_CARDS_TO_REPEAT;
    }

    private TrainingType validateTranslationRequestVariety(TrainingType trainingType, TrainingType currentTrainingType){
       return validateValue(trainingType, currentTrainingType, DefaultWordsTrainerSettings.DEFAULT_TRANSLATION_REQUEST_VARIETY);
    }
    private FlashCardsTrainingVariety validateFlashCardsTrainingVariety(FlashCardsTrainingVariety flashCardsTrainingVariety,
                                                                        FlashCardsTrainingVariety currentFlashCardsTrainingVariety){
        return validateValue(flashCardsTrainingVariety, currentFlashCardsTrainingVariety,
                DefaultWordsTrainerSettings.DEFAULT_FLASH_CARDS_TRAINING_VARIETY);
    }
    private LanguageCardsWritingEnabled validateLanguageCardsWritingEnabled(LanguageCardsWritingEnabled
                                                                                    languageCardsWritingEnabled,
                                                                            LanguageCardsWritingEnabled currentLanguageCardsWritingEnabled){
       return validateValue(languageCardsWritingEnabled, currentLanguageCardsWritingEnabled,
               DefaultWordsTrainerSettings.DEFAULT_LANGUAGE_CARD_WRITING_ENABLED);
    }

    private Boolean validateCorrectAnswersDecrementIfMistakeEnabled(Boolean correctAnswersDecrementEnabled,
                                                           Boolean currentCorrectAnswersDecrementEnabled){
        return validateValue(correctAnswersDecrementEnabled, currentCorrectAnswersDecrementEnabled,
                DefaultWordsTrainerSettings.DEFAULT_CORRECT_ANSWERS_DECREMENT_IF_MISTAKE_ENABLED);
    }

    private <T> T validateValue(T lastValue, T currentValue, T defaultValue){
return Objects.requireNonNullElseGet(lastValue, () -> (currentValue != null) ? currentValue : defaultValue);
    }
}
