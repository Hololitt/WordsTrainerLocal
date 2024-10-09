package com.hololitt.SpringBootProject.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
@Builder
public class FlashCardTrainingContext {
private List<String> wrongFlashAnswers;
private String correctAnswer;
private String question;
}
