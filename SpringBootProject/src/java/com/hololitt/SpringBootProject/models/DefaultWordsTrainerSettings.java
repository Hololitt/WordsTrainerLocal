package com.hololitt.SpringBootProject.models;

import com.hololitt.SpringBootProject.enums.FlashCardsTrainingVariety;
import com.hololitt.SpringBootProject.enums.LanguageCardsWritingEnabled;
import com.hololitt.SpringBootProject.enums.TrainingType;

public class DefaultWordsTrainerSettings {
    public static final int DEFAULT_COUNT_CORRECT_ANSWERS_TO_FINISH = 3;
    public static final int DEFAULT_COUNT_LANGUAGE_CARDS_TO_REPEAT = 5;
    public static final TrainingType DEFAULT_TRANSLATION_REQUEST_VARIETY = TrainingType.MIX;
    public static final FlashCardsTrainingVariety DEFAULT_FLASH_CARDS_TRAINING_VARIETY = FlashCardsTrainingVariety.CHOOSE_ANSWER;
    public static final LanguageCardsWritingEnabled DEFAULT_LANGUAGE_CARD_WRITING_ENABLED = LanguageCardsWritingEnabled.NO;
    public static final Boolean DEFAULT_CORRECT_ANSWERS_DECREMENT_IF_MISTAKE_ENABLED = true;
}
