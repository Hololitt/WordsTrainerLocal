package com.hololitt.SpringBootProject.services;

import com.hololitt.SpringBootProject.models.FlashCardTrainingContext;
import com.hololitt.SpringBootProject.models.LanguageCard;
import com.hololitt.SpringBootProject.models.LanguageCardContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FlashCardService {
    public List<FlashCardTrainingContext> createFlashCardTrainingList(List<LanguageCard> languageCardList){
        LanguageCardContextHolder languageCardContext = new LanguageCardContextHolder();
        List<FlashCardTrainingContext> flashCardTrainingContextList = new ArrayList<>();
        List<LanguageCard> usedCards = new ArrayList<>();
int counter = 0;
        while(counter < languageCardList.size()){
            FlashCardTrainingContext flashCardTrainingContext =
                    createFlashCardTrainingContext(languageCardList, languageCardContext, usedCards);
            flashCardTrainingContextList.add(flashCardTrainingContext);
            counter++;
        }
        return flashCardTrainingContextList;
    }
    private FlashCardTrainingContext createFlashCardTrainingContext(List<LanguageCard> languageCardList,
                                                                    LanguageCardContextHolder languageCardContext,
                                                                    List<LanguageCard> usedCards){
        languageCardContext.setLanguageCardsToLearn(languageCardList);
        languageCardContext.generateRandomValueAndLanguageCard();

        String value = languageCardContext.getRandomValue();
        LanguageCard languageCard = languageCardContext.getRandomLanguageCard();

while(usedCards.contains(languageCard)){
    languageCardContext.generateRandomValueAndLanguageCard();
    languageCard = languageCardContext.getRandomLanguageCard();
    value = languageCardContext.getRandomValue();
}
usedCards.add(languageCard);

        String correctAnswer = languageCardContext.defineCorrectAnswer(value, languageCard);
        String valueType = value.equals(languageCard.getWord()) ? "word" : "translation";

        Set<String> wrongFlashAnswers = generateWrongFlashAnswers(languageCardContext, valueType, correctAnswer);

        return FlashCardTrainingContext.builder()
                .wrongFlashAnswers(new ArrayList<>(wrongFlashAnswers))
                .correctAnswer(correctAnswer)
                .question(value)
                .build();
    }

    private Set<String> generateWrongFlashAnswers(LanguageCardContextHolder languageCardContext,
                                                  String valueType, String correctAnswer){
        Set<String> wrongFlashAnswers = new HashSet<>();
        while (wrongFlashAnswers.size() < 4) {
            String wrongAnswer = languageCardContext.generateRandomWrongFlashAnswer(valueType);

            if (!wrongAnswer.equals(correctAnswer)) {
                wrongFlashAnswers.add(wrongAnswer);
            }
        }
        return wrongFlashAnswers;
    }
}