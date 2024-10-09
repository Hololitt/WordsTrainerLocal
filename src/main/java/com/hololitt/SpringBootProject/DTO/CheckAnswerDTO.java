package com.hololitt.SpringBootProject.DTO;

import lombok.Setter;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CheckAnswerDTO {
    private boolean isCorrect;
    private String correctAnswer;

}