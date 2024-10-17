package com.hololitt.SpringBootProject.Tests;

import com.hololitt.SpringBootProject.models.LanguageCard;
import com.hololitt.SpringBootProject.models.LanguageCardContextHolder;
import com.hololitt.SpringBootProject.models.WordsTrainerSettings;
import com.hololitt.SpringBootProject.services.UserService;
import com.hololitt.SpringBootProject.services.WordsTrainerService;
import com.hololitt.SpringBootProject.services.WordsTrainerSettingsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


public class WordsTrainerServiceTest {

    @Mock
    private LanguageCardContextHolder languageCardContextHolder;

    @Mock
    private UserService userService;

    @Mock
    private WordsTrainerSettingsService wordsTrainerSettingsService;

    @InjectMocks
    private WordsTrainerService wordsTrainerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testCalculateProgressPercent_PerfectScore() {

        LanguageCard card1 = new LanguageCard();
        LanguageCard card2 = new LanguageCard();
        List<LanguageCard> languageCardsToLearn = Arrays.asList(card1, card2);

        when(userService.getUserId()).thenReturn(1L);
        WordsTrainerSettings wordsTrainerSettings = new WordsTrainerSettings();
        wordsTrainerSettings.setCorrectAnswersCountToFinish(3);
        when(wordsTrainerSettingsService.getSettingsForUser(1L)).thenReturn(wordsTrainerSettings);

        when(languageCardContextHolder.getCorrectAnswers(card1)).thenReturn(3);
        when(languageCardContextHolder.getCorrectAnswers(card2)).thenReturn(0);

        int progress = wordsTrainerService.calculateProgressPercent(languageCardsToLearn);

        assertEquals(50, progress);
    }
}
