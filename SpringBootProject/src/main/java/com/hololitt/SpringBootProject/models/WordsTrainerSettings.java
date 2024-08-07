package com.hololitt.SpringBootProject.models;

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

    public int getCountLanguageCardsToRepeat() {
        return countLanguageCardsToRepeat;
    }

    public void setCountLanguageCardsToRepeat(int countLanguageCardsToRepeat) {
        this.countLanguageCardsToRepeat = countLanguageCardsToRepeat;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getTranslationRequestVariety() {
        return translationRequestVariety;
    }

    public void setTranslationRequestVariety(String translationRequestVariety) {
        this.translationRequestVariety = translationRequestVariety;
    }

    public int getCorrectAnswersCountToFinish() {
        return correctAnswersCountToFinish;
    }

    public void setCorrectAnswersCountToFinish(int correctAnswersCountToFinish) {
        this.correctAnswersCountToFinish = correctAnswersCountToFinish;
    }
}
