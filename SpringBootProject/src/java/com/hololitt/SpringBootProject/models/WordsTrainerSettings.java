package com.hololitt.SpringBootProject.models;

import com.hololitt.SpringBootProject.enums.FlashCardsTrainingVariety;
import com.hololitt.SpringBootProject.enums.LanguageCardsWritingEnabled;
import com.hololitt.SpringBootProject.enums.TrainingType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class WordsTrainerSettings {
    private int correctAnswersCountToFinish;
    private TrainingType translationRequestVariety;
    private int countLanguageCardsToRepeat;
    private long userId;
    private FlashCardsTrainingVariety flashCardsTrainingVariety;
    private LanguageCardsWritingEnabled languageCardsWritingEnabled;
    private Boolean correctAnswersDecrementIfMistakeEnabled;
}
