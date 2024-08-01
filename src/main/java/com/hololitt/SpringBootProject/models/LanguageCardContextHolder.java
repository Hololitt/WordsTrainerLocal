package com.hololitt.SpringBootProject.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LanguageCardContextHolder {
    private List<LanguageCard> languageCardsToLearn = new ArrayList<>();
    private final List<LanguageCard> learnedLanguageCards = new ArrayList<>();
    private final List<Integer> correctAnswers = new ArrayList<>();

private String randomValue;
private LanguageCard randomLanguageCard;
private int countCorrectAnswersToFinish = 3;
public void setCountCorrectAnswersToFinish(int countCorrectAnswersToFinish){
    this.countCorrectAnswersToFinish = countCorrectAnswersToFinish;
}
public int getCountCorrectAnswersToFinish(){
    return countCorrectAnswersToFinish;
}
public void generateRandomValueAndLanguageCard(){
    Random random = new Random();
    boolean chooseWord = random.nextBoolean();
    randomLanguageCard = languageCardsToLearn.get(random.nextInt(languageCardsToLearn.size()));
    randomValue = chooseWord ? randomLanguageCard.getWord() : randomLanguageCard.getTranslation();
}
public List<Integer> getCorrectAnswers(){
    return correctAnswers;
}
    public LanguageCard getRandomLanguageCard(){
      return randomLanguageCard;
    }
    public String getRandomValue() {
        return randomValue;
    }
    public String getCorrectAnswer(String value, LanguageCard languageCard){
        String trueAnswer;
        String valueType = value.equals(languageCard.getWord()) ? "word" : "translation";
        if (valueType.equals("word")) {
            trueAnswer = languageCard.getTranslation();
        } else {
            trueAnswer = languageCard.getWord();
        }
        return trueAnswer;
    }
    public boolean isCorrectAnswer(LanguageCard languageCard, String answer, String correctAnswer) {
if(correctAnswers.isEmpty()){
    for(int i = 0; i<languageCardsToLearn.size(); i++){
        correctAnswers.add(i, 0);
    }
}
            if (answer.equals(correctAnswer)) {
                int index = languageCardsToLearn.indexOf(languageCard);
                int currentCorrectAnswers = correctAnswers.get(index);
                currentCorrectAnswers++;
                correctAnswers.set(index, currentCorrectAnswers);
                return true;
            }

        return false;
    }
    public int getCorrectAnswers(LanguageCard languageCard){
        int index = languageCardsToLearn.indexOf(languageCard);
        if(correctAnswers.isEmpty()){
            return 0;
        }
        return correctAnswers.get(index);
    }

    public void removeFromCorrectAnswers(LanguageCard languageCard){
    try{
        int index = languageCardsToLearn.indexOf(languageCard);
        correctAnswers.remove(index);
    }catch(UnsupportedOperationException e){
        e.printStackTrace();
    }

    }
    public void cleanUpContext(){
    clearList(languageCardsToLearn);
    clearList(languageCardsToLearn);
    clearList(correctAnswers);
    randomLanguageCard = null;
    randomValue = null;
    }
    private void clearList(List<?> list) {
        if (list != null) {
            list.clear();
        }
    }
public void removeFromLanguageCardsToLearn(LanguageCard languageCard){
        languageCardsToLearn.remove(languageCard);
}

    public void addLanguageCardToLearn(LanguageCard languageCard){
        languageCardsToLearn.add(languageCard);
    }
    public void addLearnedLanguageCard(LanguageCard languageCard){
        learnedLanguageCards.add(languageCard);
    }
    public List<LanguageCard> getLearnedLanguageCards(){
        return learnedLanguageCards;
    }
    public List<LanguageCard> getLanguageCardsToLearn(){
        return languageCardsToLearn;
    }
    public void setLanguageCardsToLearn(List<LanguageCard> languageCardsToLearn){
        this.languageCardsToLearn = languageCardsToLearn;
    }
}
