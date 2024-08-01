package com.hololitt.SpringBootProject.DTO;

public class CheckAnswerDTO {
    private boolean isCorrect;
    private String correctAnswer;

    public CheckAnswerDTO() {
    }

    public CheckAnswerDTO(boolean isCorrect, String correctAnswer) {
        this.isCorrect = isCorrect;
        this.correctAnswer = correctAnswer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }


    @Override
    public String toString() {
        return "CheckAnswerDTO{" +
                "isCorrect=" + isCorrect +
                ", correctAnswer='" + correctAnswer + '\'' +
                '}';
    }
}