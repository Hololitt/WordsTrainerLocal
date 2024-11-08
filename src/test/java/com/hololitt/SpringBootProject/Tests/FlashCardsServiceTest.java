package com.hololitt.SpringBootProject.Tests;

import java.util.Arrays;
import java.util.List;

import com.hololitt.SpringBootProject.models.FlashCard;
import com.hololitt.SpringBootProject.models.FlashCardTrainingContext;
import com.hololitt.SpringBootProject.models.LanguageCard;
import com.hololitt.SpringBootProject.services.FlashCardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FlashCardsServiceTest {

    private FlashCardService flashCardService;

    private List<LanguageCard> languageCardList;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        flashCardService = new FlashCardService();
        languageCardList = Arrays.asList(
                new LanguageCard("Hello", "Привет"),
                new LanguageCard("Goodbye", "До свидания"),
                new LanguageCard("Thank you", "Спасибо")
        );
    }

    @Test
    public void testCreateFlashCardTrainingList() {
        List<FlashCardTrainingContext> trainingList = flashCardService.createFlashCardTrainingList(languageCardList);

        assertNotNull(trainingList);
        assertFalse(trainingList.isEmpty());

        for (FlashCardTrainingContext context : trainingList) {
            assertNotNull(context.getQuestion());
            assertNotNull(context.getCorrectAnswer());
            assertNotNull(context.getWrongFlashAnswers());
        }
    }

    @Test
    public void testCreateMatchingTrainingList(){
        List<FlashCard> flashCardList = flashCardService.createMatchingTrainingList(languageCardList);

        assertNotNull(flashCardList);
        assertFalse(flashCardList.isEmpty());
        assertEquals(flashCardList.size(), 3);
    }
}
