package com.hololitt.SpringBootProject.Tests;

import com.hololitt.SpringBootProject.enums.FlashCardsTrainingVariety;
import com.hololitt.SpringBootProject.enums.TrainingType;
import com.hololitt.SpringBootProject.models.WordsTrainerSettings;
import com.hololitt.SpringBootProject.services.WordsTrainerSettingsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SpringBootTest
public class WordsTrainerSettingsServiceTest {
private WordsTrainerSettingsService wordsTrainerSettingsService;
@Mock
    private WordsTrainerSettings mockSettings;

    @BeforeEach
    public void setUp(){
        wordsTrainerSettingsService = new WordsTrainerSettingsService();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSetAllSettings() {
        mockSettings.setUserId(1L);
mockSettings.setTranslationRequestVariety(TrainingType.TRANSLATION_TO_WORD);
        System.out.println(mockSettings.getCountLanguageCardsToRepeat());
        wordsTrainerSettingsService.setAllSettings(mockSettings);

        WordsTrainerSettings result = wordsTrainerSettingsService.getSettingsForUser(1L);
        System.out.println(result.getTranslationRequestVariety() + " " + result.getCountLanguageCardsToRepeat());
        assertNotNull(result);
        assertEquals(5, result.getCountLanguageCardsToRepeat());
    }
}
