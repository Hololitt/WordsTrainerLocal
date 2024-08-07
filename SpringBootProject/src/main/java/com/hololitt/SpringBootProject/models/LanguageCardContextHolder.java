package com.hololitt.SpringBootProject.models;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class LanguageCardContextHolder {
    private List<LanguageCard> languageCardsToLearn = new ArrayList<>();
    private final List<LanguageCard> learnedLanguageCards = new ArrayList<>();
    private final List<Integer> correctAnswers = new ArrayList<>();
    private final Map<LanguageCard, Integer> mistakesCountDuringTraining = new HashMap<>();
private String randomValue;
private LanguageCard randomLanguageCard;
private final Random random = new Random();
public Map<LanguageCard, Integer> getAllMistakesDuringTraining(){
    return mistakesCountDuringTraining;
}
public void setContext(){
    initializeCorrectAnswersList();
    initializeMistakesCountDuringTrainingList();
}
    public void incrementMistakesCountDuringTraining(LanguageCard languageCard){
       int currentMistakesCount = mistakesCountDuringTraining.get(languageCard);
        if (mistakesCountDuringTraining.containsKey(languageCard)) {
            mistakesCountDuringTraining.replace(languageCard, currentMistakesCount,currentMistakesCount+1);
        }
    }
public void generateRandomValueAndLanguageCard(){
    boolean chooseWord = random.nextBoolean();
    randomLanguageCard = languageCardsToLearn.get(random.nextInt(languageCardsToLearn.size()));
    randomValue = chooseWord ? randomLanguageCard.getWord() : randomLanguageCard.getTranslation();
}
public void generateRandomLanguageCardAndTranslation(){
    randomLanguageCard = languageCardsToLearn.get(random.nextInt(languageCardsToLearn.size()));
    randomValue = randomLanguageCard.getTranslation();
}
public void generateRandomLanguageCardAndWord(){
    randomLanguageCard = languageCardsToLearn.get(random.nextInt(languageCardsToLearn.size()));
    randomValue = randomLanguageCard.getWord();
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
    private void initializeMistakesCountDuringTrainingList(){
       if(mistakesCountDuringTraining.isEmpty()){
           for (LanguageCard languageCard : languageCardsToLearn) {
               mistakesCountDuringTraining.put(languageCard, 0);
           }
       }
    }
    private void initializeCorrectAnswersList(){
        if(correctAnswers.isEmpty()){
            for(int i = 0; i<languageCardsToLearn.size(); i++){
                correctAnswers.add(i, 0);
            }
        }
    }
    public boolean isCorrectAnswer(LanguageCard languageCard, String answer, String correctAnswer) {
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
public void decrementCorrectAnswersCount(LanguageCard languageCard){
    int index = languageCardsToLearn.indexOf(languageCard);
    int currentCorrectAnswersCount = correctAnswers.get(index) - 1;
    correctAnswers.set(index, currentCorrectAnswersCount);
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
    clearList(learnedLanguageCards);
    clearList(correctAnswers);
    clearMap(mistakesCountDuringTraining);
    randomLanguageCard = null;
    randomValue = null;
    }
    private void clearList(List<?> list) {
        if (list != null && !list.isEmpty()) {
            list.clear();
        }
    }
    private void clearMap(Map<?, ?> map){
    if(map !=null && !map.isEmpty()){
        map.clear();
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
