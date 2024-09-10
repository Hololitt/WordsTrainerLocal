package com.hololitt.SpringBootProject.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WordsTrainerSettings {
    public WordsTrainerSettings(int correctAnswersCountToFinish, String translationRequestVariety,
                                int countLanguageCardsToRepeat, long userId){
        this.correctAnswersCountToFinish = correctAnswersCountToFinish;
        this.translationRequestVariety = translationRequestVariety;
        this.userId = userId;
        this.countLanguageCardsToRepeat = countLanguageCardsToRepeat;
    }
    public WordsTrainerSettings(){}
    private int correctAnswersCountToFinish;
    private String translationRequestVariety;
    private int countLanguageCardsToRepeat;
    private long userId;
}
