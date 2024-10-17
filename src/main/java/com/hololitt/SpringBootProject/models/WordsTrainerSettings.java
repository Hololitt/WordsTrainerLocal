package com.hololitt.SpringBootProject.models;

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
    private String translationRequestVariety;
    private int countLanguageCardsToRepeat;
    private long userId;
    private String flashCardsTrainingVariety;
}
