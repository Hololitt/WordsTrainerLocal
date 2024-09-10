package com.hololitt.SpringBootProject.DTO;

public class TrainingContextDTO {
    private String randomValue;
    private int correctAnswersCount;
    private int languageCardsToLearnAmount;
    private int learnedLanguageCardsAmount;
    private int correctAnswersCountToFinish;

    // Геттеры и сеттеры
    public String getRandomValue() {
        return randomValue;
    }

    public void setRandomValue(String randomValue) {
        this.randomValue = randomValue;
    }

    public int getCorrectAnswersCount() {
        return correctAnswersCount;
    }

    public void setCorrectAnswersCount(int correctAnswersCount) {
        this.correctAnswersCount = correctAnswersCount;
    }

    public int getLanguageCardsToLearnAmount() {
        return languageCardsToLearnAmount;
    }

    public void setLanguageCardsToLearnAmount(int languageCardsToLearnAmount) {
        this.languageCardsToLearnAmount = languageCardsToLearnAmount;
    }

    public int getLearnedLanguageCardsAmount() {
        return learnedLanguageCardsAmount;
    }

    public void setLearnedLanguageCardsAmount(int learnedLanguageCardsAmount) {
        this.learnedLanguageCardsAmount = learnedLanguageCardsAmount;
    }

    public int getCorrectAnswersCountToFinish() {
        return correctAnswersCountToFinish;
    }

    public void setCorrectAnswersCountToFinish(int correctAnswersCountToFinish) {
        this.correctAnswersCountToFinish = correctAnswersCountToFinish;
    }
}
