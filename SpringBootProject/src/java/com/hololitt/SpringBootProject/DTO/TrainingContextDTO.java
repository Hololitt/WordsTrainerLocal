package com.hololitt.SpringBootProject.DTO;
import lombok.Builder;
import lombok.Setter;
import lombok.Getter;

@Setter
@Getter
@Builder
public class TrainingContextDTO {
    private String randomValue;
    private int correctAnswersCount;
    private int languageCardsToLearnAmount;
    private int learnedLanguageCardsAmount;
    private int correctAnswersCountToFinish;

}
