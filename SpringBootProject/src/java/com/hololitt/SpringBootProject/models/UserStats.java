package com.hololitt.SpringBootProject.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserStats {
    private String username;
private Integer totalMistakesCount;
private Integer countLanguageCards;
private Integer languageCardsRepetitionCount;


}
