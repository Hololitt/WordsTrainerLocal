package com.hololitt.SpringBootProject.models;

import com.hololitt.SpringBootProject.enums.FlashCardsTrainingVariety;
import com.hololitt.SpringBootProject.enums.TrainingType;

public class DefaultWordsTrainerSettings {
    public static final int DEFAULT_COUNT_CORRECT_ANSWERS_TO_FINISH = 3;
    public static final int DEFAULT_COUNT_LANGUAGE_CARDS_TO_REPEAT = 5;
    public static final TrainingType DEFAULT_TRANSLATION_REQUEST_VARIETY = TrainingType.MIX;
    public static final FlashCardsTrainingVariety DEFAULT_FLASH_CARDS_TRAINING_VARIETY = FlashCardsTrainingVariety.CHOOSE_ANSWER;

}
