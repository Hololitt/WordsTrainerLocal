package com.hololitt.SpringBootProject.models;

import com.hololitt.SpringBootProject.enums.FlashCardsTrainingVariety;
import com.hololitt.SpringBootProject.enums.TrainingType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WordsTrainerSettings {
    private int correctAnswersCountToFinish;
    private TrainingType translationRequestVariety;
    private int countLanguageCardsToRepeat;
    private long userId;
    private FlashCardsTrainingVariety flashCardsTrainingVariety;
}
